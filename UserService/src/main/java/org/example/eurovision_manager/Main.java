package org.example.eurovision_manager;

import io.javalin.Javalin;
import org.example.eurovision_manager.model.entity.User;
import org.example.eurovision_manager.model.service.UserService;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        Javalin app = Javalin.create().start(7002);

        app.post("/login", ctx -> {
            User credentials = ctx.bodyAsClass(User.class);
            User loggedInUser = userService.login(credentials.getUsername(), credentials.getPassword());

            if (loggedInUser != null) {
                loggedInUser.setPassword(null);
                ctx.status(200).json(loggedInUser);
            } else {
                ctx.status(401).result("Invalid credentials");
            }
        });
    }
}