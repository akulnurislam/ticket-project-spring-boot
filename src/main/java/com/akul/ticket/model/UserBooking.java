package com.akul.ticket.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record UserBooking(
        UUID bookingId,
        UUID userId,
        UUID ticketId,
        String username,
        String ticketName,
        String ticketCategory,
        BigDecimal price,
        Integer quantity,
        BigDecimal totalPrice,
        Date createdAt,
        Date updatedAt) {
}
