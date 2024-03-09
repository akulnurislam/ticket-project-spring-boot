package com.akul.ticket.service;

import com.akul.ticket.model.Ticket;

import java.util.List;

public interface TicketService {
    Ticket create(Ticket ticket);

    List<Ticket> getAll(String status);
}
