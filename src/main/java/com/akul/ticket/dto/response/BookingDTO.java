package com.akul.ticket.dto.response;

import com.akul.ticket.model.UserBooking;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Builder
@Getter
public class BookingDTO {

    private UUID id;

    @JsonProperty("user_id")
    private UUID userId;

    @JsonProperty("ticket_id")
    private UUID ticketId;

    @Schema(defaultValue = "im-people")
    private String username;

    @Schema(defaultValue = "Dangdut Mania Concert 2024")
    @JsonProperty("ticket_name")
    private String ticketName;

    @Schema(defaultValue = "Concert")
    @JsonProperty("ticket_category")
    private String ticketCategory;

    @Schema(defaultValue = "250000")
    private BigDecimal price;

    @Schema(defaultValue = "1")
    private Integer quantity;

    @Schema(defaultValue = "250000")
    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("created_at")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("updated_at")
    private Date updatedAt;

    public static BookingDTO mapFromModel(@NonNull UserBooking model) {
        return BookingDTO.builder()
                .id(model.bookingId())
                .userId(model.userId())
                .ticketId(model.ticketId())
                .username(model.username())
                .ticketName(model.ticketName())
                .ticketCategory(model.ticketCategory())
                .price(model.price())
                .quantity(model.quantity())
                .totalPrice(model.totalPrice())
                .createdAt(model.createdAt())
                .updatedAt(model.updatedAt())
                .build();
    }
}
