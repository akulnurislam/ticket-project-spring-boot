package com.akul.ticket.model;

import com.akul.ticket.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_ticket", columnDefinition = "uuid", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ticket_category", nullable = false)
    private TicketCategory ticketCategory;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "available_from", nullable = false)
    private java.sql.Time availableFrom;

    @Column(name = "available_to", nullable = false)
    private java.sql.Time availableTo;

    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status = TicketStatus.UPCOMING;

    @Column(name = "quota", nullable = false)
    private Integer quota;

    @Column(name = "quota_remaining", nullable = false)
    private Integer quotaRemaining;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}
