package com.ezequiel.supportdesk_api.ticket.history;

import com.ezequiel.supportdesk_api.ticket.Ticket;
import com.ezequiel.supportdesk_api.ticket.TicketStatus;
import com.ezequiel.supportdesk_api.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TicketHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus newStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id")
    private User changedBy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id")
    private Ticket ticket;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
