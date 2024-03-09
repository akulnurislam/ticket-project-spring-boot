package com.akul.ticket.dto.request;

import com.akul.ticket.annotation.ValidUUID;
import com.akul.ticket.model.Booking;
import com.akul.ticket.model.Ticket;
import com.akul.ticket.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.UUID;

@Data
public class BookingCreateDTO {

    @Schema(defaultValue = "valid-ticket-uuid")
    @NotBlank
    @ValidUUID
    @JsonProperty("ticket_id")
    private String ticketId;

    @Schema(defaultValue = "1")
    @NotNull(message = "is required")
    @PositiveOrZero
    private Integer quantity;

    public Booking mapToModel(String username) {
        var user = new User();
        user.setUsername(username);

        var ticket = new Ticket();
        ticket.setId(UUID.fromString(ticketId));

        var booking = new Booking();
        booking.setUser(user);
        booking.setTicket(ticket);
        booking.setQuantity(quantity);

        return booking;
    }
}
