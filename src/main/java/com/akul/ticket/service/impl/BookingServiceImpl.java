package com.akul.ticket.service.impl;

import com.akul.ticket.enums.TicketStatus;
import com.akul.ticket.exception.ConflictException;
import com.akul.ticket.exception.NotFoundException;
import com.akul.ticket.model.Booking;
import com.akul.ticket.model.UserBooking;
import com.akul.ticket.repository.BookingRepository;
import com.akul.ticket.repository.TicketRepository;
import com.akul.ticket.repository.UserRepository;
import com.akul.ticket.service.BookingService;
import com.akul.ticket.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public UserBooking create(@NonNull Booking booking) {
        if (Objects.isNull(booking.getUser())) {
            throw new NotFoundException("user not found");
        }

        if (Objects.isNull(booking.getTicket())) {
            throw new NotFoundException("ticket not found");
        }

        // user exists
        var username = booking.getUser().getUsername();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("user not found"));

        // ticket exists
        var ticketId = booking.getTicket().getId();
        var ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("ticket not found"));

        // validate only available ticket could be booked
        var status = ticket.getStatus();
        if (!TicketStatus.AVAILABLE.equals(status)) {
            throw new ConflictException(
                    String.format("status ticket is %s, only AVAILABLE ticket could be booked", status));
        }

        // validate hour availability
        var currentHour = Time.valueOf(LocalTime.now());
        var availableFrom = ticket.getAvailableFrom();
        var availableTo = ticket.getAvailableTo();
        if (currentHour.before(availableFrom) || currentHour.after(availableTo)) {
            throw new ConflictException(
                    String.format("ticket can only bookings between %s to %s",
                            DateUtil.formatTime(availableFrom), DateUtil.formatTime(availableTo)));
        }

        // save booking
        var now = new Date();
        var userId = user.getId();
        booking.getUser().setId(userId);
        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);
        var bookingCreated = bookingRepository.save(booking);

        // update quota remaining ticket
        // update status ticket if quota remaining zero then sold_out
        var quantity = bookingCreated.getQuantity();
        var quotaRemaining = ticket.getQuotaRemaining() - quantity;
        ticket.setQuotaRemaining(quotaRemaining);
        if (quotaRemaining == 0) {
            ticket.setStatus(TicketStatus.SOLD_OUT);
        }
        ticketRepository.save(ticket);

        var ticketCategory = ticket.getTicketCategory().getName();
        var totalPrice = ticket.getPrice().multiply(BigDecimal.valueOf(quantity));
        return new UserBooking(
                bookingCreated.getId(),
                userId,
                ticketId,
                username,
                ticket.getName(),
                ticketCategory,
                ticket.getPrice(),
                quantity,
                totalPrice,
                now,
                now);
    }
}
