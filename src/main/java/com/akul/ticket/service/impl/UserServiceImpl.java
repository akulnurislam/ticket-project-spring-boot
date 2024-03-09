package com.akul.ticket.service.impl;

import com.akul.ticket.model.User;
import com.akul.ticket.repository.UserRepository;
import com.akul.ticket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAll() {
        var sort = Sort.by(Sort.Direction.DESC, "createdAt");
        return userRepository.findAll(sort);
    }
}
