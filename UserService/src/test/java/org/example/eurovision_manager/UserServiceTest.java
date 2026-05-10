package org.example.eurovision_manager;

import org.example.eurovision_manager.model.entity.User;
import org.example.eurovision_manager.model.service.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    public void testLoginQuery() {
        UserService service = new UserService();

        User loggedInUser = service.login("admin_ebu", "admin123");

        assertNotNull(loggedInUser);
        assertEquals("ADMIN", loggedInUser.getRole());
    }
}