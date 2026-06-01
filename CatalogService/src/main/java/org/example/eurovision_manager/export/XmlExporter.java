package org.example.eurovision_manager.export;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.eurovision_manager.model.entity.Entry;
import java.util.List;

public class XmlExporter extends DataExportTemplate {

    @Override
    protected String transformData(List<Entry> entries) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();

        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        return xmlMapper.writeValueAsString(entries);
    }
}