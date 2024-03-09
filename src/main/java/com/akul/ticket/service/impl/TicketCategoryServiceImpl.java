package com.akul.ticket.service.impl;

import com.akul.ticket.exception.NotFoundException;
import com.akul.ticket.model.TicketCategory;
import com.akul.ticket.repository.TicketCategoryRepository;
import com.akul.ticket.service.TicketCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TicketCategoryServiceImpl implements TicketCategoryService {

    private final TicketCategoryRepository ticketCategoryRepository;

    @Override
    public TicketCategory create(TicketCategory ticketCategory) {
        return ticketCategoryRepository.save(ticketCategory);
    }

    @Override
    public List<TicketCategory> getAll() {
        return ticketCategoryRepository.findAll();
    }

    @Override
    public boolean isExists(UUID id) {
        return ticketCategoryRepository.findById(id).isPresent();
    }
}
