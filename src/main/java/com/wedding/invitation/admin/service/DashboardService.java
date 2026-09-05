package com.wedding.invitation.admin.service;

import com.wedding.invitation.admin.dto.DashboardResponse;
import com.wedding.invitation.admin.dto.RecentActivityResponse;
import com.wedding.invitation.domain.AccessType;
import com.wedding.invitation.domain.InvitationView;
import com.wedding.invitation.domain.Message;
import com.wedding.invitation.invitation.repository.GuestRepository;
import com.wedding.invitation.invitation.repository.InvitationViewRepository;
import com.wedding.invitation.invitation.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final GuestRepository guestRepository;
    private final InvitationViewRepository invitationViewRepository;
    private final MessageRepository messageRepository;

    public DashboardResponse getDashboard() {
        long totalGuestCount = guestRepository.count();
        long activeGuestCount = guestRepository.countByIsActiveTrue();

        long linkViewCount = invitationViewRepository.countByAccessType(AccessType.LINK);
        long qrViewCount = invitationViewRepository.countByAccessType(AccessType.QR);
        long totalViewCount = linkViewCount + qrViewCount;

        long totalMessageCount = messageRepository.count();

        return new DashboardResponse(
                totalGuestCount,
                activeGuestCount,
                totalViewCount,
                linkViewCount,
                qrViewCount,
                totalMessageCount
        );
    }

    public RecentActivityResponse getRecentActivity() {
        List<RecentActivityResponse.RecentView> recentViews = invitationViewRepository
                .findTop10ByOrderByViewedAtDesc()
                .stream()
                .map(this::toRecentView)
                .toList();

        List<RecentActivityResponse.RecentMessage> recentMessages = messageRepository
                .findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(this::toRecentMessage)
                .toList();

        return new RecentActivityResponse(recentViews, recentMessages);
    }

    private RecentActivityResponse.RecentView toRecentView(InvitationView view) {
        return new RecentActivityResponse.RecentView(
                view.getGuest() == null ? null : view.getGuest().getName(),
                view.getAccessType(),
                view.getViewedAt()
        );
    }

    private RecentActivityResponse.RecentMessage toRecentMessage(Message message) {
        return new RecentActivityResponse.RecentMessage(
                message.getGuestName(),
                message.getAccessType(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}