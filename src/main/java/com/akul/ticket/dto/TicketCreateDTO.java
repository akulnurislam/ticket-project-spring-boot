package com.akul.ticket.dto;

import com.akul.ticket.annotation.ValidUUID;
import com.akul.ticket.enums.TicketStatus;
import com.akul.ticket.model.Ticket;
import com.akul.ticket.model.TicketCategory;
import com.akul.ticket.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TicketCreateDTO {

    @Schema(defaultValue = "valid-uuid-here")
    @NotBlank
    @ValidUUID
    @JsonProperty("ticket_category_id")
    private String ticketCategoryId;

    @Schema(defaultValue = "Dangdut Mania Concert 2024")
    @NotBlank
    @Size(min = 4, max = 255)
    private String name;

    @Schema(defaultValue = "10:00 AM")
    @NotBlank
    @Pattern(regexp = "(1[0-2]|0?[1-9]):[0-5][0-9] (AM|PM)", message = "expected format: 'HH:mm AM' or 'HH:mm PM'")
    @JsonProperty("available_from")
    private String availableFrom;

    @Schema(defaultValue = "10:20 AM")
    @NotBlank
    @Pattern(regexp = "(1[0-2]|0?[1-9]):[0-5][0-9] (AM|PM)", message = "expected format: 'HH:mm AM' or 'HH:mm PM'")
    @JsonProperty("available_to")
    private String availableTo;

    @Schema(defaultValue = "250000")
    @NotNull(message = "is required")
    @PositiveOrZero
    private BigDecimal price;

    @Schema(defaultValue = "AVAILABLE")
    private TicketStatus status;

    @Schema(defaultValue = "10000")
    @NotNull(message = "is required")
    @PositiveOrZero
    private Integer quota;

    public Ticket mapToModel() {
        var category = new TicketCategory();
        category.setId(UUID.fromString(ticketCategoryId));

        var ticket = new Ticket();
        ticket.setTicketCategory(category);
        ticket.setName(name);
        ticket.setAvailableFrom(DateUtil.parseTime(availableFrom));
        ticket.setAvailableTo(DateUtil.parseTime(availableTo));
        ticket.setPrice(price);
        ticket.setStatus(status);
        ticket.setQuota(quota);

        return ticket;
    }
}
