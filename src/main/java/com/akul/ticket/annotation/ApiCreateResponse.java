package com.akul.ticket.annotation;

import com.akul.ticket.dto.ErrorDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "created"),
        @ApiResponse(responseCode = "400", description = "bad request", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
        @ApiResponse(responseCode = "409", description = "conflict or duplicate", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
        @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = ErrorDTO.class))),
})
public @interface ApiCreateResponse {
}
