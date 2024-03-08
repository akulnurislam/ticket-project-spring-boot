package com.akul.ticket.service.impl;

import com.akul.ticket.model.User;
import com.akul.ticket.repository.UserRepository;
import com.akul.ticket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }
}
