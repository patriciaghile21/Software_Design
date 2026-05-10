package org.example.eurovision_manager.export;

import org.example.eurovision_manager.model.entity.Entry;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class DataExportTemplate {

    public final void exportData(List<Entry> entries, String filePath) {
        if (entries == null || entries.isEmpty()) {
            System.out.println("No data to export.");
            return;
        }

        try {
            String transformedData = transformData(entries);
            writeToFile(transformedData, filePath);
            System.out.println("Export completed successfully at: " + filePath);
        } catch (Exception e) {
            System.out.println("Error during export: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected abstract String transformData(List<Entry> entries) throws Exception;

    private void writeToFile(String data, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(data);
        }
    }
}