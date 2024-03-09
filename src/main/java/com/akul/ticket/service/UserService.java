package com.akul.ticket.service;

import com.akul.ticket.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user);

    Optional<User> getByUsername(String username);

    List<User> getAll();
}
