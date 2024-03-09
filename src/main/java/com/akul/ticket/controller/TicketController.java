package com.akul.ticket.controller;

import com.akul.ticket.annotation.ApiCreateResponse;
import com.akul.ticket.annotation.ApiListResponse;
import com.akul.ticket.dto.ErrorDTO;
import com.akul.ticket.dto.TicketCreateDTO;
import com.akul.ticket.dto.TicketDTO;
import com.akul.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
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
@RequestMapping("/tickets")
@Tag(name = "Tickets")
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Create a Ticket")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiCreateResponse
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    public ResponseEntity<TicketDTO> create(@RequestBody @Valid @NonNull TicketCreateDTO dto) {
        var ticket = ticketService.create(dto.mapToModel());
        return ResponseEntity.created(URI.create("/tickets")).body(TicketDTO.mapFromModel(ticket));
    }

    @Operation(summary = "Get Tickets")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiListResponse
    public ResponseEntity<List<TicketDTO>> getAll(
            @Parameter(description = "filter by ticket status [ALL, UPCOMING, CANCELLED, AVAILABLE, ENDED, SOLD_OUT")
            @Pattern(regexp = "ALL|UPCOMING|CANCELLED|AVAILABLE|ENDED|SOLD_OUT", message = "status must be one of [ALL, UPCOMING, CANCELLED, AVAILABLE, ENDED, SOLD_OUT")
            @RequestParam(required = false, defaultValue = "AVAILABLE") String status) {
        var tickets = ticketService.getAll(status);
        return ResponseEntity.ok(TicketDTO.mapFromModels(tickets));
    }
}
