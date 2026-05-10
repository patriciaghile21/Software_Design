package org.example.eurovision_manager.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.eurovision_manager.model.entity.Entry;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonExportStrategy implements ExportStrategy {

    @Override
    public void exportData(List<Entry> entries, String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            mapper.writeValue(new File(filePath), entries);
            System.out.println("JSON export completed successfully at: " + filePath);
        } catch (IOException e) {
            System.out.println("Error during JSON export: " + e.getMessage());
            e.printStackTrace();
        }
    }
}