package org.example.eurovision_manager.export;

import org.example.eurovision_manager.model.entity.Entry;
import java.util.List;

public interface ExportStrategy {
    void exportData(List<Entry> entries, String filePath);
}