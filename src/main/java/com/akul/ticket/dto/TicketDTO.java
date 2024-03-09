package com.akul.ticket.dto;

import com.akul.ticket.enums.TicketStatus;
import com.akul.ticket.model.Ticket;
import com.akul.ticket.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Builder
@Getter
public class TicketDTO {

    private UUID id;

    @JsonProperty("ticket_category_id")
    private UUID ticketCategoryId;

    @Schema(defaultValue = "Dangdut Mania Concert 2024")
    private String name;

    @Schema(defaultValue = "10:00 AM")
    @JsonProperty("available_from")
    private String availableFrom;

    @Schema(defaultValue = "10:20 AM")
    @JsonProperty("available_to")
    private String availableTo;

    @Schema(defaultValue = "250000")
    private BigDecimal price;

    @Schema(defaultValue = "AVAILABLE")
    private TicketStatus status;

    @Schema(defaultValue = "10000")
    private Integer quota;

    @Schema(defaultValue = "4500")
    private Integer quotaRemaining;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("created_at")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("updated_at")
    private Date updatedAt;

    public static TicketDTO mapFromModel(Ticket ticket) {
        return TicketDTO.builder()
                .id(ticket.getId())
                .ticketCategoryId(ticket.getTicketCategory().getId())
                .name(ticket.getName())
                .availableFrom(DateUtil.formatTime(ticket.getAvailableFrom()))
                .availableTo(DateUtil.formatTime(ticket.getAvailableTo()))
                .price(ticket.getPrice())
                .status(ticket.getStatus())
                .quota(ticket.getQuota())
                .quotaRemaining(ticket.getQuotaRemaining())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}
