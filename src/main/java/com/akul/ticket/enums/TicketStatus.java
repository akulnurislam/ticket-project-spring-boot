package com.akul.ticket.enums;

import org.springframework.lang.Nullable;

public enum TicketStatus {
    UPCOMING,
    CANCELLED,
    AVAILABLE,
    ENDED,
    SOLD_OUT;

    @Nullable
    public static TicketStatus valueOfOrNull(String name) {
        try {
            return TicketStatus.valueOf(name);
        } catch (IllegalArgumentException ignore) {
            return null;
        }
    }
}
