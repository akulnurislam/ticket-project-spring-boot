package com.akul.ticket.service;

import com.akul.ticket.model.User;
import com.akul.ticket.repository.UserRepository;
import com.akul.ticket.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserService userService = new UserServiceImpl(userRepository);

    @Test
    public void testCreate() {
        var user = new User();
        user.setUsername("Test User");

        when(userRepository.save(user)).thenReturn(user);

        var savedUser = userService.create(user);

        assertNotNull(savedUser);
        assertEquals("Test User", savedUser.getUsername());
    }

    @Test
    public void testGetByUsername() {
        var user = new User();
        user.setUsername("Test User");

        when(userRepository.findByUsername("Test User")).thenReturn(Optional.of(user));

        var foundUser = userService.getByUsername("Test User");

        assertTrue(foundUser.isPresent());
        assertEquals("Test User", foundUser.get().getUsername());
    }

    @Test
    public void testGetAll() {
        var user = new User();
        user.setUsername("Test User");

        when(userRepository.findAll(any(Sort.class))).thenReturn(Collections.singletonList(user));

        var users = userService.getAll();

        assertNotNull(users);
        assertFalse(users.isEmpty());
        assertEquals("Test User", users.getFirst().getUsername());
    }
}
