package com.akul.ticket.dto;

import com.akul.ticket.model.TicketCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TicketCategoryCreateDTO {

    @Schema(defaultValue = "Concert", minLength = 4, maxLength = 100)
    @NotBlank
    @Size(min = 4, max = 100)
    private String name;

    public TicketCategory mapToModel() {
        var ticketCategory = new TicketCategory();
        ticketCategory.setName(name);
        return ticketCategory;
    }
}
