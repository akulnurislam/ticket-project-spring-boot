package com.akul.ticket.controller;

import com.akul.ticket.dto.UserCreateDTO;
import com.akul.ticket.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a User")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> create(@RequestBody @Valid UserCreateDTO dto) {
        userService.create(dto.mapToModel());
        return ResponseEntity.created(URI.create("/users")).build();
    }
}
