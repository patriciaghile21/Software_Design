package org.example.eurovision_manager;

import org.example.eurovision_manager.model.entity.ErrorResponse;
import io.javalin.Javalin;
import org.example.eurovision_manager.model.entity.User;
import org.example.eurovision_manager.model.service.UserService;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> it.anyHost());
            });
        }).start(7002);

        app.post("/login", ctx -> {
            User credentials = ctx.bodyAsClass(User.class);
            User loggedInUser = userService.login(credentials.getUsername(), credentials.getPassword());

            if (loggedInUser != null) {
                loggedInUser.setPassword(null);
                ctx.status(200).json(loggedInUser);
            } else {
                ctx.status(401).json(new ErrorResponse("Invalid credentials", 401));
            }
        });
    }
}