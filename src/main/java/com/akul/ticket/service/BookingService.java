package com.akul.ticket.service;

import com.akul.ticket.model.Booking;
import com.akul.ticket.model.UserBooking;

public interface BookingService {
    UserBooking create(Booking booking);
}
