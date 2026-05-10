package org.example.eurovision_manager;

import org.example.eurovision_manager.command.Command;
import org.example.eurovision_manager.command.CreateEntryCommand;
import org.example.eurovision_manager.model.entity.Entry;
import org.example.eurovision_manager.model.entity.User;
import org.example.eurovision_manager.model.service.EntryService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CatalogServiceTest {

    @Test
    public void testQueryOperation() {
        EntryService service = EntryService.getInstance();
        List<Entry> entries = service.getAllEntries();
        assertNotNull(entries);
    }

    @Test
    public void testTemplatePatternExport() {
        EntryService service = EntryService.getInstance();
        List<Entry> entries = service.getAllEntries();

        org.example.eurovision_manager.export.DataExportTemplate csvExporter =
                new org.example.eurovision_manager.export.CsvExporter();

        org.example.eurovision_manager.export.DataExportTemplate jsonExporter =
                new org.example.eurovision_manager.export.JsonExporter();

        org.example.eurovision_manager.export.DataExportTemplate xmlExporter =
                new org.example.eurovision_manager.export.XmlExporter();

        String csvFilePath = "test_catalog.csv";
        String jsonFilePath = "test_catalog.json";
        String xmlFilePath = "test_catalog.xml";

        assertDoesNotThrow(() -> {
            csvExporter.exportData(entries, csvFilePath);
            jsonExporter.exportData(entries, jsonFilePath);
            xmlExporter.exportData(entries, xmlFilePath);
        });

        java.io.File csvFile = new java.io.File(csvFilePath);
        java.io.File jsonFile = new java.io.File(jsonFilePath);
        java.io.File xmlFile = new java.io.File(xmlFilePath);

        assertTrue(csvFile.exists());
        assertTrue(jsonFile.exists());
        assertTrue(xmlFile.exists());

        //if (csvFile.exists()) csvFile.delete();
        //if (jsonFile.exists()) jsonFile.delete();
        //if (xmlFile.exists()) xmlFile.delete();
    }
    @Test
    public void testCommandOperation() {
        EntryService service = EntryService.getInstance();

        Entry testEntry = new Entry();
        testEntry.setCountry("TestCountry");
        testEntry.setArtist("TestArtist");
        testEntry.setTitle("TestSong");
        testEntry.setReleaseYear(2026);

        User adminUser = new User();
        adminUser.setRole("ADMIN");
        adminUser.setCountry("TestCountry");

        Command createCommand = new CreateEntryCommand(service, testEntry, adminUser);

        assertDoesNotThrow(() -> {
            createCommand.execute();
        });
    }
}