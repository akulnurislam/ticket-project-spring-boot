package com.akul.ticket.util;

import org.springframework.lang.NonNull;

public class StringUtil {

    @NonNull
    public static String camelToSnake(@NonNull String camelCase) {
        return camelCase.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
    }
}
