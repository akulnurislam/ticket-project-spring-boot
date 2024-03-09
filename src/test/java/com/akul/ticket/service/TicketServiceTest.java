package com.akul.ticket.service;

import com.akul.ticket.enums.TicketStatus;
import com.akul.ticket.exception.ConflictException;
import com.akul.ticket.exception.NotFoundException;
import com.akul.ticket.model.Ticket;
import com.akul.ticket.model.TicketCategory;
import com.akul.ticket.repository.TicketRepository;
import com.akul.ticket.service.impl.TicketServiceImpl;
import com.akul.ticket.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TicketServiceTest {

    private final TicketRepository ticketRepository = Mockito.mock(TicketRepository.class);
    private final TicketCategoryService ticketCategoryService = Mockito.mock(TicketCategoryService.class);
    private final TicketService ticketService = new TicketServiceImpl(ticketRepository, ticketCategoryService);

    @Test
    public void testCreate() {
        var ticketCategory = new TicketCategory();
        ticketCategory.setId(UUID.randomUUID());

        var ticket = new Ticket();
        ticket.setTicketCategory(ticketCategory);
        ticket.setAvailableFrom(DateUtil.parseTime("10:00 AM"));
        ticket.setAvailableTo(DateUtil.parseTime("10:20 AM"));
        ticket.setQuota(10);

        when(ticketCategoryService.isExists(ticketCategory.getId())).thenReturn(true);
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        var result = ticketService.create(ticket);

        assertEquals(ticket, result);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    public void testCreateWithInvalidTicketCategory() {
        var ticketCategory = new TicketCategory();
        ticketCategory.setId(UUID.randomUUID());

        var ticket = new Ticket();
        ticket.setTicketCategory(ticketCategory);

        when(ticketCategoryService.isExists(ticketCategory.getId())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> ticketService.create(ticket));
    }

    @Test
    public void testCreateWithInvalidAvailableDates() {
        var ticketCategory = new TicketCategory();
        ticketCategory.setId(UUID.randomUUID());

        var ticket = new Ticket();
        ticket.setTicketCategory(ticketCategory);
        ticket.setAvailableFrom(DateUtil.parseTime("10:01 AM"));
        ticket.setAvailableTo(DateUtil.parseTime("10:00 AM"));

        when(ticketCategoryService.isExists(ticketCategory.getId())).thenReturn(true);

        assertThrows(ConflictException.class, () -> ticketService.create(ticket));
    }

    @Test
    public void testGetAll() {
        var ticketCategory = new TicketCategory();
        ticketCategory.setId(UUID.randomUUID());

        var ticket = new Ticket();
        ticket.setTicketCategory(ticketCategory);
        ticket.setAvailableFrom(DateUtil.parseTime("10:00 AM"));
        ticket.setAvailableTo(DateUtil.parseTime("10:20 AM"));
        ticket.setQuota(10);
        ticket.setStatus(TicketStatus.AVAILABLE);

        when(ticketRepository.findAll(any(Sort.class))).thenReturn(Collections.singletonList(ticket));

        var result = ticketService.getAll("ALL");

        assertEquals(1, result.size());
        verify(ticketRepository, times(1)).findAll(any(Sort.class));
    }
}
