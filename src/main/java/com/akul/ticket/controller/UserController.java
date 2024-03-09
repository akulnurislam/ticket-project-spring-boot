package com.akul.ticket.controller;

import com.akul.ticket.annotation.ApiCreateResponse;
import com.akul.ticket.dto.UserCreateDTO;
import com.akul.ticket.dto.UserDTO;
import com.akul.ticket.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a User")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiCreateResponse
    public ResponseEntity<UserDTO> create(@RequestBody @Valid @NonNull UserCreateDTO dto) {
        var user = userService.create(dto.mapToModel());
        return ResponseEntity.created(URI.create("/users")).body(UserDTO.mapFromModel(user));
    }

    @Operation(summary = "Get Users")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok")
    })
    public ResponseEntity<List<UserDTO>> getAll() {
        var users = userService.getAll();
        return ResponseEntity.ok(UserDTO.mapFromModels(users));
    }
}
