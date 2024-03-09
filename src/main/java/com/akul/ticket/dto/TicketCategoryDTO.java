package com.akul.ticket.dto;

import com.akul.ticket.model.TicketCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class TicketCategoryDTO {

    private UUID id;

    @Schema(defaultValue = "Concert")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("created_at")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("updated_at")
    private Date updatedAt;

    public static TicketCategoryDTO mapFromModel(@NonNull TicketCategory model) {
        return TicketCategoryDTO.builder()
                .id(model.getId())
                .name(model.getName())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .build();
    }

    public static List<TicketCategoryDTO> mapFromModels(@NonNull List<TicketCategory> models) {
        return models.stream()
                .map(TicketCategoryDTO::mapFromModel)
                .toList();
    }
}
