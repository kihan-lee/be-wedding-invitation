package com.wedding.invitation.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "invitation_view", schema = "wedding-invitation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InvitationView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @Column(name = "access_type", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private AccessType accessType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime viewedAt;

    @PrePersist
    protected void onCreate() {
        this.viewedAt = LocalDateTime.now();
    }

    @Builder
    public InvitationView(Guest guest, AccessType accessType) {
        this.guest = guest;
        this.accessType = accessType;
    }
}