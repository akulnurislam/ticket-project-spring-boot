package com.akul.ticket.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilTest {

    @Test
    public void testCamelToSnake() {
        var camelCase = "camelCaseString";
        var snakeCase = StringUtil.camelToSnake(camelCase);
        assertEquals("camel_case_string", snakeCase);

        camelCase = "AnotherCamelCaseString";
        snakeCase = StringUtil.camelToSnake(camelCase);
        assertEquals("another_camel_case_string", snakeCase);
    }
}
