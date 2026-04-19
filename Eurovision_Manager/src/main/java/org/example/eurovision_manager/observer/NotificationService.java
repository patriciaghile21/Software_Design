package org.example.eurovision_manager.observer;

import org.example.eurovision_manager.model.entity.Entry;
import org.example.eurovision_manager.model.entity.User;

public class NotificationService implements EntryObserver {

    @Override
    public void onEntryAction(Entry entry, User user, String actionType) {
        System.out.println("\nEMAIL NOTIFICATION SYSTEM");
        System.out.println("To: " + user.getEmail());
        System.out.println("Subject: Eurovision Database Update - " + actionType);

        String message = String.format(
                "Hello %s,\n\nThe following entry has been %s in the system:\n" +
                        "Artist: %s\n" +
                        "Song Title: %s\n" +
                        "Country: %s\n" +
                        "Action performed by: %s\n",
                user.getUsername(), actionType, entry.getArtist(),
                entry.getTitle(), entry.getCountry(), user.getUsername()
        );

        System.out.println("Body:\n" + message);
    }
}