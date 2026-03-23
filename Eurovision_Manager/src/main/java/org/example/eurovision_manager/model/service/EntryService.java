package org.example.eurovision_manager.model.service;

import org.example.eurovision_manager.model.entity.Entry;
import org.example.eurovision_manager.model.entity.User;
import org.example.eurovision_manager.model.repository.EntryRepository;
import java.util.List;

public class EntryService {
    private final EntryRepository entryRepository = new EntryRepository();

    public List<Entry> getAllEntries() {
        return entryRepository.findAll();
    }

    public List<Entry> searchEntries(String keyword) {
        return entryRepository.search(keyword);
    }

    public List<Entry> getSortedEntries(String sortBy, boolean ascending) {
        String direction = ascending ? "ASC" : "DESC";
        return entryRepository.findAllSorted(sortBy, direction);
    }

    public void createEntry(Entry entry, User currentUser) {
        if ("ADMIN".equals(currentUser.getRole())) {
            entryRepository.create(entry);
        } else if ("HOD".equals(currentUser.getRole()) && entry.getCountry().equals(currentUser.getCountry())) {
            entryRepository.create(entry);
        } else {
            throw new RuntimeException("Access denied: You can only add entries for your country.");
        }
    }

    public void updateEntry(Entry entry, User currentUser) {
        if ("ADMIN".equals(currentUser.getRole())) {
            entryRepository.update(entry);
        } else if ("HOD".equals(currentUser.getRole()) && entry.getCountry().equals(currentUser.getCountry())) {
            entryRepository.update(entry);
        } else {
            throw new RuntimeException("Access denied: Only administrators can edit entries.");
        }
    }

    public void deleteEntry(int id, User currentUser) {
        Entry entryToDelete = entryRepository.findById(id);

        if (entryToDelete == null) {
            throw new RuntimeException("Error: Entry not found.");
        }
        if ("ADMIN".equals(currentUser.getRole())) {
            entryRepository.delete(id);
        } else if ("HOD".equals(currentUser.getRole()) && entryToDelete.getCountry().equals(currentUser.getCountry())) {
            entryRepository.delete(id);
        } else {
            throw new RuntimeException("Access denied: You can only delete entries from " + currentUser.getCountry());
        }
    }
}