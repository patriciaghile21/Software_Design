package org.example.eurovision_manager.command;

import org.example.eurovision_manager.model.entity.Entry;
import org.example.eurovision_manager.model.entity.User;
import org.example.eurovision_manager.model.service.EntryService;

public class CreateEntryCommand implements Command {
    private final EntryService service;
    private final Entry entry;
    private final User user;

    public CreateEntryCommand(EntryService service, Entry entry, User user) {
        this.service = service;
        this.entry = entry;
        this.user = user;
    }

    @Override
    public void execute() {
        service.createEntry(entry, user);
    }
}