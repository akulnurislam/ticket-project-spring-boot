package com.akul.ticket.dto;

import com.akul.ticket.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDTO {

    @Schema(defaultValue = "im-people", minLength = 4, maxLength = 50)
    @NotBlank
    @Size(min = 4, max = 50)
    private String username;

    public User mapToModel() {
        User user = new User();
        user.setUsername(username);
        return user;
    }
}
