package org.example.eurovision_manager.export;

import org.example.eurovision_manager.model.entity.Entry;
import java.util.List;

public class CsvExporter extends DataExportTemplate {

    @Override
    protected String transformData(List<Entry> entries) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Country,Artist,Title,Release_Year\n");

        for (Entry entry : entries) {
            sb.append(String.format("%d,%s,%s,%s,%d\n",
                    entry.getId(),
                    entry.getCountry(),
                    entry.getArtist(),
                    entry.getTitle(),
                    entry.getReleaseYear()));
        }
        return sb.toString();
    }
}