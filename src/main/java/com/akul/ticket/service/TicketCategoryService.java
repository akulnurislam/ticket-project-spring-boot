package com.akul.ticket.service;

import com.akul.ticket.model.TicketCategory;

import java.util.List;

public interface TicketCategoryService {
    TicketCategory create(TicketCategory ticketCategory);
    List<TicketCategory> getAll();
}
