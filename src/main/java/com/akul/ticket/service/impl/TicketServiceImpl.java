package com.akul.ticket.service.impl;

import com.akul.ticket.enums.TicketStatus;
import com.akul.ticket.exception.ConflictException;
import com.akul.ticket.exception.NotFoundException;
import com.akul.ticket.model.Ticket;
import com.akul.ticket.repository.TicketRepository;
import com.akul.ticket.service.TicketCategoryService;
import com.akul.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketCategoryService ticketCategoryService;

    @Override
    public Ticket create(@NonNull Ticket ticket) {
        if (Objects.isNull(ticket.getTicketCategory()) ||
                !ticketCategoryService.isExists(ticket.getTicketCategory().getId())) {
            throw new NotFoundException("ticket category not found");
        }

        if (Objects.isNull(ticket.getStatus())) {
            ticket.setStatus(TicketStatus.UPCOMING);
        }

        ticket.setQuotaRemaining(ticket.getQuota());

        var availableFrom = ticket.getAvailableFrom();
        var availableTo = ticket.getAvailableTo();
        if (Objects.isNull(availableFrom) || Objects.isNull(availableTo) || !availableFrom.before(availableTo)) {
            throw new ConflictException("available_from must be less than available_to");
        }

        return ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getAll(String status) {
        TicketStatus ticketStatus = TicketStatus.valueOfOrNull(status);

        if (Objects.isNull(ticketStatus)) {
            return ticketRepository.findAll();
        }

        return ticketRepository.findAllByStatus(ticketStatus);
    }
}
