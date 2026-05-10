package org.example.eurovision_manager.model.service;

import org.example.eurovision_manager.model.entity.User;
import org.example.eurovision_manager.model.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository = new UserRepository();

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}