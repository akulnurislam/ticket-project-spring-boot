package com.akul.ticket.repository;

import com.akul.ticket.enums.TicketStatus;
import com.akul.ticket.model.Ticket;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findAllByStatus(TicketStatus status, Sort sort);
}
