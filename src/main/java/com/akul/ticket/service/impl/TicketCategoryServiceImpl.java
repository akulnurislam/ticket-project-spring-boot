package com.akul.ticket.service.impl;

import com.akul.ticket.model.TicketCategory;
import com.akul.ticket.repository.TicketCategoryRepository;
import com.akul.ticket.service.TicketCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
