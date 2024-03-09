package com.akul.ticket.controller;

import com.akul.ticket.annotation.ApiCreateResponse;
import com.akul.ticket.dto.ErrorDTO;
import com.akul.ticket.dto.request.BookingCreateDTO;
import com.akul.ticket.dto.response.BookingDTO;
import com.akul.ticket.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
@Tag(name = "Bookings")
@SecurityRequirement(name = "api-key")
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "Create a Booking")
    @ApiCreateResponse
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "not found", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookingDTO> create(
            @Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody @Valid @NonNull BookingCreateDTO dto) {
        var booking = bookingService.create(dto.mapToModel(authorization));
        return ResponseEntity.created(URI.create("/bookings")).body(BookingDTO.mapFromModel(booking));
    }
}
