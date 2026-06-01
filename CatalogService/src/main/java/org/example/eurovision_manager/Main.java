package org.example.eurovision_manager;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.example.eurovision_manager.command.*;
import org.example.eurovision_manager.model.entity.Entry;
import org.example.eurovision_manager.model.entity.User;
import org.example.eurovision_manager.model.entity.ErrorResponse;
import org.example.eurovision_manager.model.service.EntryService;
import org.example.eurovision_manager.export.*;

import io.javalin.websocket.WsContext;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import java.io.File;
import java.nio.file.Files;

public class Main {

    private static Set<WsContext> chatClients = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        EntryService entryService = EntryService.getInstance();
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> it.anyHost());
            });
        }).start(7001);

        app.get("/entries", ctx -> {
            String keyword = ctx.queryParam("search");
            if (keyword != null) {
                ctx.status(200).json(entryService.searchEntries(keyword));
            } else {
                ctx.status(200).json(entryService.getAllEntries());
            }
        });

        app.post("/entries", ctx -> {
            try {
                Entry newEntry = ctx.bodyAsClass(Entry.class);
                User currentUser = extractUserFromHeaders(ctx);
                Command command = new CreateEntryCommand(entryService, newEntry, currentUser);
                command.execute();
                ctx.status(201).result("Entry created successfully");
            } catch (Exception e) {
                ctx.status(403).json(new ErrorResponse(e.getMessage(), 403));
            }
        });

        app.put("/entries/{id}", ctx -> {
            try {
                int id = Integer.parseInt(ctx.pathParam("id"));
                Entry updatedEntry = ctx.bodyAsClass(Entry.class);
                updatedEntry.setId(id);
                User currentUser = extractUserFromHeaders(ctx);
                Command command = new UpdateEntryCommand(entryService, updatedEntry, currentUser);
                command.execute();
                ctx.status(200).result("Entry updated successfully");
            } catch (Exception e) {
                ctx.status(403).json(new ErrorResponse(e.getMessage(), 403));
            }
        });

        app.delete("/entries/{id}", ctx -> {
            try {
                int id = Integer.parseInt(ctx.pathParam("id"));
                User currentUser = extractUserFromHeaders(ctx);
                Command command = new DeleteEntryCommand(entryService, id, currentUser);
                command.execute();
                ctx.status(200).result("Entry deleted successfully");
            } catch (Exception e) {
                ctx.status(403).json(new ErrorResponse(e.getMessage(), 403));
            }
        });

        app.get("/entries/export", ctx -> {
            try {
                String format = ctx.queryParam("format");
                if (format == null) {
                    ctx.status(400).json(new ErrorResponse("Format not specified", 400));
                    return;
                }

                var entries = entryService.getAllEntries();
                DataExportTemplate exporter = null;
                String contentType = "";
                String fileExtension = "";

                switch (format.toLowerCase()) {
                    case "csv":
                        exporter = new CsvExporter();
                        contentType = "text/csv";
                        fileExtension = ".csv";
                        break;
                    case "json":
                        exporter = new JsonExporter();
                        contentType = "application/json";
                        fileExtension = ".json";
                        break;
                    case "xml":
                        exporter = new XmlExporter();
                        contentType = "application/xml";
                        fileExtension = ".xml";
                        break;
                    default:
                        ctx.status(400).json(new ErrorResponse("Unsupported format", 400));
                        return;
                }

                File tempFile = File.createTempFile("export_", fileExtension);
                exporter.exportData(entries, tempFile.getAbsolutePath());
                byte[] fileBytes = Files.readAllBytes(tempFile.toPath());
                tempFile.delete();

                ctx.header("Content-Disposition", "attachment; filename=eurovision_data" + fileExtension);
                ctx.contentType(contentType);
                ctx.status(200).result(fileBytes);
            } catch (Exception e) {
                ctx.status(500).json(new ErrorResponse(e.getMessage(), 500));
            }
        });

        app.ws("/chat", ws -> {
            ws.onConnect(ctx -> {
                chatClients.add(ctx);
            });

            ws.onClose(ctx -> {
                chatClients.remove(ctx);
            });

            ws.onMessage(ctx -> {
                String message = ctx.message();
                for (WsContext client : chatClients) {
                    if (client.session.isOpen()) {
                        client.send(message);
                    }
                }
            });
        });
    }

    private static User extractUserFromHeaders(Context ctx) {
        User user = new User();
        user.setRole(ctx.header("X-User-Role"));
        user.setCountry(ctx.header("X-User-Country"));
        user.setEmail(ctx.header("X-User-Email"));
        return user;
    }
}