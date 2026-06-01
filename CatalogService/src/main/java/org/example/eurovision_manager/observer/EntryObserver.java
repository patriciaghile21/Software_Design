package org.example.eurovision_manager.observer;

import org.example.eurovision_manager.model.entity.Entry;
import org.example.eurovision_manager.model.entity.User;

public interface EntryObserver {
    void onEntryAction(Entry entry, User user, String actionType);
}