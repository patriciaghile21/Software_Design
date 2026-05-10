package org.example.eurovision_manager;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.example.eurovision_manager.command.*;
import org.example.eurovision_manager.model.entity.Entry;
import org.example.eurovision_manager.model.entity.User;
import org.example.eurovision_manager.model.service.EntryService;

public class Main {
    public static void main(String[] args) {
        EntryService entryService = EntryService.getInstance();
        Javalin app = Javalin.create().start(7001);

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
                ctx.status(403).result(e.getMessage());
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
                ctx.status(403).result(e.getMessage());
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
                ctx.status(403).result(e.getMessage());
            }
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