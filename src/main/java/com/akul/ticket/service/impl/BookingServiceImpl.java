package com.akul.ticket.service.impl;

import com.akul.ticket.model.Booking;
import com.akul.ticket.repository.BookingRepository;
import com.akul.ticket.service.BookingService;
import com.akul.ticket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;

    @Override
    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }
}
