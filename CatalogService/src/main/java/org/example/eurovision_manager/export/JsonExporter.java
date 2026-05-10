package org.example.eurovision_manager.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.eurovision_manager.model.entity.Entry;
import java.util.List;

public class JsonExporter extends DataExportTemplate {

    @Override
    protected String transformData(List<Entry> entries) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(entries);
    }
}