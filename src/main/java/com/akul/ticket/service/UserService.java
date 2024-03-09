package com.akul.ticket.service;

import com.akul.ticket.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    boolean isExistsByUsername(String username);

    List<User> getAll();
}
