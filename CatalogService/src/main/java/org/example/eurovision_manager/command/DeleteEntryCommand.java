package org.example.eurovision_manager.command;

import org.example.eurovision_manager.model.entity.User;
import org.example.eurovision_manager.model.service.EntryService;

public class DeleteEntryCommand implements Command {
    private final EntryService service;
    private final int id;
    private final User user;

    public DeleteEntryCommand(EntryService service, int id, User user) {
        this.service = service;
        this.id = id;
        this.user = user;
    }

    @Override
    public void execute() {
        service.deleteEntry(id, user);
    }
}