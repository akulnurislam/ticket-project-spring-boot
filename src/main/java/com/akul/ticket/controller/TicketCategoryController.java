package com.akul.ticket.controller;

import com.akul.ticket.annotation.ApiCreateResponse;
import com.akul.ticket.dto.TicketCategoryCreateDTO;
import com.akul.ticket.dto.TicketCategoryDTO;
import com.akul.ticket.service.TicketCategoryService;
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
@RequestMapping("/ticket-categories")
@Tag(name = "Ticket Categories")
public class TicketCategoryController {

    private final TicketCategoryService ticketCategoryService;

    @Operation(summary = "Create a Ticket Category")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiCreateResponse
    public ResponseEntity<TicketCategoryDTO> create(@RequestBody @Valid @NonNull TicketCategoryCreateDTO dto) {
        var ticketCategory = ticketCategoryService.create(dto.mapToModel());
        return ResponseEntity.created(URI.create("/users")).body(TicketCategoryDTO.mapFromModel(ticketCategory));
    }

    @Operation(summary = "Get Ticket Categories")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok")
    })
    public ResponseEntity<List<TicketCategoryDTO>> getAll() {
        var ticketCategories = ticketCategoryService.getAll();
        return ResponseEntity.ok(TicketCategoryDTO.mapFromModels(ticketCategories));
    }
}
