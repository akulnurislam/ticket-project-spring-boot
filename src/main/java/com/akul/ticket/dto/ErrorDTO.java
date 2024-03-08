package com.akul.ticket.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {
    private String timestamp;
    private int status;
    private String error;
    private String path;
    private List<String> fields;
}
