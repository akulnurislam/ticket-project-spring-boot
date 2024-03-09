package com.akul.ticket.service;

import com.akul.ticket.model.TicketCategory;

import java.util.List;
import java.util.UUID;

public interface TicketCategoryService {
    TicketCategory create(TicketCategory ticketCategory);
    List<TicketCategory> getAll();
    boolean isExists(UUID id);
}
