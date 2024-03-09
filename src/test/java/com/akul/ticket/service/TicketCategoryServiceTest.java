package com.akul.ticket.service;

import com.akul.ticket.model.TicketCategory;
import com.akul.ticket.repository.TicketCategoryRepository;
import com.akul.ticket.service.impl.TicketCategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TicketCategoryServiceTest {

    private final TicketCategoryRepository ticketCategoryRepository = Mockito.mock(TicketCategoryRepository.class);
    private final TicketCategoryService ticketCategoryService = new TicketCategoryServiceImpl(ticketCategoryRepository);

    @Test
    public void testCreate() {
        var ticketCategory = new TicketCategory();

        when(ticketCategoryRepository.save(ticketCategory)).thenReturn(ticketCategory);

        var result = ticketCategoryService.create(ticketCategory);

        assertEquals(ticketCategory, result);
        verify(ticketCategoryRepository, times(1)).save(ticketCategory);
    }

    @Test
    public void testGetAll() {
        var ticketCategory1 = new TicketCategory();
        var ticketCategory2 = new TicketCategory();
        var ticketCategories = Arrays.asList(ticketCategory1, ticketCategory2);

        when(ticketCategoryRepository.findAll()).thenReturn(ticketCategories);

        var result = ticketCategoryService.getAll();

        assertEquals(2, result.size());
        verify(ticketCategoryRepository, times(1)).findAll();
    }

    @Test
    public void testIsExists() {
        var id = UUID.randomUUID();

        when(ticketCategoryRepository.findById(id)).thenReturn(Optional.of(new TicketCategory()));

        var result = ticketCategoryService.isExists(id);

        assertTrue(result);
        verify(ticketCategoryRepository, times(1)).findById(id);
    }
}
