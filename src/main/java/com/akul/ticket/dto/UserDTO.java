package com.akul.ticket.dto;

import com.akul.ticket.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserDTO {

    @Schema(defaultValue = "im-people")
    private String username;

    public static UserDTO mapFromModel(User model) {
        return UserDTO.builder()
                .username(model.getUsername())
                .build();
    }

    public static List<UserDTO> mapFromModels(List<User> models) {
        return models.stream()
                .map(UserDTO::mapFromModel)
                .toList();
    }
}
