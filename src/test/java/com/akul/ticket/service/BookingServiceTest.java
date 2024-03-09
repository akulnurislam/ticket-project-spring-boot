package com.akul.ticket.service;

import com.akul.ticket.enums.TicketStatus;
import com.akul.ticket.exception.ConflictException;
import com.akul.ticket.exception.NotFoundException;
import com.akul.ticket.model.Booking;
import com.akul.ticket.model.Ticket;
import com.akul.ticket.model.TicketCategory;
import com.akul.ticket.model.User;
import com.akul.ticket.repository.BookingRepository;
import com.akul.ticket.repository.TicketRepository;
import com.akul.ticket.repository.UserRepository;
import com.akul.ticket.service.impl.BookingServiceImpl;
import com.akul.ticket.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    private final BookingRepository bookingRepository = Mockito.mock(BookingRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final TicketRepository ticketRepository = Mockito.mock(TicketRepository.class);
    private final BookingService bookingService = new BookingServiceImpl(
            bookingRepository,
            userRepository,
            ticketRepository);

    @Test
    public void testCreateNullUser() {
        var booking = new Booking();

        var ex = assertThrows(NotFoundException.class, () -> bookingService.create(booking));
        assertEquals("user not found", ex.getMessage());
    }

    @Test
    public void testCreateNullTicket() {
        var user = new User();
        user.setUsername("user1");

        var booking = new Booking();
        booking.setUser(user);

        var ex = assertThrows(NotFoundException.class, () -> bookingService.create(booking));
        assertEquals("ticket not found", ex.getMessage());
    }

    @Test
    public void testCreateThrowsUserNotFound() {
        var user = new User();
        user.setUsername("user1");

        var ticket = new Ticket();
        ticket.setId(UUID.randomUUID());

        var booking = new Booking();
        booking.setUser(user);
        booking.setTicket(ticket);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());

        var ex = assertThrows(NotFoundException.class, () -> bookingService.create(booking));
        assertEquals("user not found", ex.getMessage());
    }

    @Test
    public void testCreateThrowsTicketNotFound() {
        var user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("user1");

        var ticketId = UUID.randomUUID();
        var ticket = new Ticket();
        ticket.setId(ticketId);

        var booking = new Booking();
        booking.setUser(user);
        booking.setTicket(ticket);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        var ex = assertThrows(NotFoundException.class, () -> bookingService.create(booking));
        assertEquals("ticket not found", ex.getMessage());
    }

    @Test
    public void testCreateWithUnavailableTicket() {
        var user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("user1");

        var ticketId = UUID.randomUUID();
        var ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setStatus(TicketStatus.SOLD_OUT);

        var booking = new Booking();
        booking.setUser(user);
        booking.setTicket(ticket);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        var ex = assertThrows(ConflictException.class, () -> bookingService.create(booking));
        assertEquals("status ticket is SOLD_OUT, only AVAILABLE ticket could be booked", ex.getMessage());
    }

    @Test
    public void testCreateWithTicketOutsideBookingHoursEarly() {
        var user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("user1");

        var ticketId = UUID.randomUUID();
        var ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setStatus(TicketStatus.AVAILABLE);

        var currentTime = LocalTime.now();
        var from = Time.valueOf(currentTime.plusHours(1));
        var to = Time.valueOf(currentTime.plusHours(1).plusMinutes(30));

        ticket.setAvailableFrom(from);
        ticket.setAvailableTo(to);

        var booking = new Booking();
        booking.setUser(user);
        booking.setTicket(ticket);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        var ex = assertThrows(ConflictException.class, () -> bookingService.create(booking));
        var expected = String.format("ticket can only bookings between %s to %s", DateUtil.formatTime(from), DateUtil.formatTime(to));
        assertEquals(expected, ex.getMessage());
    }

    @Test
    public void testCreateWithTicketOutsideBookingHoursLate() {
        var user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("user1");

        var ticketId = UUID.randomUUID();
        var ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setStatus(TicketStatus.AVAILABLE);

        var currentTime = LocalTime.now();
        var from = Time.valueOf(currentTime.minusHours(2));
        var to = Time.valueOf(currentTime.minusHours(1));

        ticket.setAvailableFrom(from);
        ticket.setAvailableTo(to);

        var booking = new Booking();
        booking.setUser(user);
        booking.setTicket(ticket);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        var ex = assertThrows(ConflictException.class, () -> bookingService.create(booking));
        var expected = String.format("ticket can only bookings between %s to %s", DateUtil.formatTime(from), DateUtil.formatTime(to));
        assertEquals(expected, ex.getMessage());
    }

    @Test
    public void testCreateWithQuotaRemainingInsufficient() {
        var username = "user1";
        var user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(username);

        var ticketId = UUID.randomUUID();
        var ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setName("live-music");
        ticket.setStatus(TicketStatus.AVAILABLE);
        ticket.setQuotaRemaining(3);

        var currentTime = LocalTime.now();
        var from = Time.valueOf(currentTime.minusHours(1));
        var to = Time.valueOf(currentTime.plusHours(1));

        ticket.setAvailableFrom(from);
        ticket.setAvailableTo(to);

        var booking = new Booking();
        booking.setId(UUID.randomUUID());
        booking.setQuantity(6);
        booking.setUser(user);
        booking.setTicket(ticket);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(bookingRepository.save(booking)).thenReturn(booking);

        var ex = assertThrows(ConflictException.class, () -> bookingService.create(booking));
        var expected = "quota remaining insufficient";
        assertEquals(expected, ex.getMessage());

        verify(userRepository, times(1)).findByUsername(username);
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    public void testCreate() {
        var userId = UUID.randomUUID();
        var username = "user1";
        var user = new User();
        user.setId(userId);
        user.setUsername(username);

        var ticketCategory = "concert";
        var category = new TicketCategory();
        category.setName(ticketCategory);

        var ticketId = UUID.randomUUID();
        var ticketName = "live-music";
        var ticketPrice = BigDecimal.valueOf(100000);
        var quotaRemaining = 7;
        var ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setName(ticketName);
        ticket.setStatus(TicketStatus.AVAILABLE);
        ticket.setTicketCategory(category);
        ticket.setPrice(ticketPrice);
        ticket.setQuotaRemaining(quotaRemaining);

        var currentTime = LocalTime.now();
        var from = Time.valueOf(currentTime.minusHours(1));
        var to = Time.valueOf(currentTime.plusHours(1));

        ticket.setAvailableFrom(from);
        ticket.setAvailableTo(to);

        var bookingId = UUID.randomUUID();
        var quantity = 2;
        var booking = new Booking();
        booking.setId(bookingId);
        booking.setQuantity(quantity);
        booking.setUser(user);
        booking.setTicket(ticket);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        var result = bookingService.create(booking);
        var totalPrice = ticketPrice.multiply(BigDecimal.valueOf(quantity));
        var expectedQuotaRemaining = quotaRemaining - quantity;


        assertEquals(bookingId, result.bookingId());
        assertEquals(userId, result.userId());
        assertEquals(ticketId, result.ticketId());
        assertEquals(username, result.username());
        assertEquals(ticketName, result.ticketName());
        assertEquals(ticketCategory, result.ticketCategory());
        assertEquals(ticketPrice, result.price());
        assertEquals(quantity, result.quantity());
        assertEquals(totalPrice, result.totalPrice());

        assertEquals(expectedQuotaRemaining, ticket.getQuotaRemaining());

        verify(userRepository, times(1)).findByUsername(username);
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(bookingRepository, times(1)).save(booking);
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    public void testCreateThenSoldOut() {
        var userId = UUID.randomUUID();
        var username = "user1";
        var user = new User();
        user.setId(userId);
        user.setUsername(username);

        var ticketCategory = "concert";
        var category = new TicketCategory();
        category.setName(ticketCategory);

        var ticketId = UUID.randomUUID();
        var ticketName = "live-music";
        var ticketPrice = BigDecimal.valueOf(100000);
        var quotaRemaining = 2;
        var ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setName(ticketName);
        ticket.setStatus(TicketStatus.AVAILABLE);
        ticket.setTicketCategory(category);
        ticket.setPrice(ticketPrice);
        ticket.setQuotaRemaining(quotaRemaining);

        var currentTime = LocalTime.now();
        var from = Time.valueOf(currentTime.minusHours(1));
        var to = Time.valueOf(currentTime.plusHours(1));

        ticket.setAvailableFrom(from);
        ticket.setAvailableTo(to);

        var bookingId = UUID.randomUUID();
        var quantity = 2;
        var booking = new Booking();
        booking.setId(bookingId);
        booking.setQuantity(quantity);
        booking.setUser(user);
        booking.setTicket(ticket);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        var result = bookingService.create(booking);
        var totalPrice = ticketPrice.multiply(BigDecimal.valueOf(quantity));
        var expectedQuotaRemaining = 0;
        var expectedStatus = TicketStatus.SOLD_OUT;


        assertEquals(bookingId, result.bookingId());
        assertEquals(userId, result.userId());
        assertEquals(ticketId, result.ticketId());
        assertEquals(username, result.username());
        assertEquals(ticketName, result.ticketName());
        assertEquals(ticketCategory, result.ticketCategory());
        assertEquals(ticketPrice, result.price());
        assertEquals(quantity, result.quantity());
        assertEquals(totalPrice, result.totalPrice());

        assertEquals(expectedQuotaRemaining, ticket.getQuotaRemaining());
        assertEquals(expectedStatus, ticket.getStatus());

        verify(userRepository, times(1)).findByUsername(username);
        verify(ticketRepository, times(1)).findById(ticketId);
        verify(bookingRepository, times(1)).save(booking);
        verify(ticketRepository, times(1)).save(ticket);
    }
}
