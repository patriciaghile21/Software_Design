package org.example.eurovision_manager.export;

import org.example.eurovision_manager.model.entity.Entry;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CsvExportStrategy implements ExportStrategy {

    @Override
    public void exportData(List<Entry> entries, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("ID,Country,Artist,Title,Release_Year");

            for (Entry entry : entries) {
                writer.printf("%d,%s,%s,%s,%d\n",
                        entry.getId(),
                        entry.getCountry(),
                        entry.getArtist(),
                        entry.getTitle(),
                        entry.getReleaseYear());
            }
            System.out.println("CSV export completed successfully at: " + filePath);
        } catch (IOException e) {
            System.out.println("Error during CSV export: " + e.getMessage());
            e.printStackTrace();
        }
    }
}