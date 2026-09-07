package com.wedding.invitation.admin.service;

import com.wedding.invitation.admin.dto.GuestCreateRequest;
import com.wedding.invitation.admin.dto.GuestCreateResponse;
import com.wedding.invitation.common.exception.CustomException;
import com.wedding.invitation.common.exception.ErrorCode;
import com.wedding.invitation.domain.Guest;
import com.wedding.invitation.invitation.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuestService {

    private final GuestRepository guestRepository;

    @Transactional
    public List<GuestCreateResponse> create(List<GuestCreateRequest> requests) {
        return requests.stream()
                .map(this::createOne)
                .toList();
    }

    public List<GuestCreateResponse> getGuests(Boolean active, Guest.Side side) {
        List<Guest> guests;
        if (active != null && side != null) {
            guests = guestRepository.findAllByIsActiveAndSideOrderByNameAsc(active, side);
        } else if (active != null) {
            guests = guestRepository.findAllByIsActiveOrderByNameAsc(active);
        } else if (side != null) {
            guests = guestRepository.findAllBySideOrderByNameAsc(side);
        } else {
            guests = guestRepository.findAllByOrderByNameAsc();
        }

        return guests.stream()
                .map(this::toResponse)
                .toList();
    }

    private GuestCreateResponse createOne(GuestCreateRequest request) {
        if (guestRepository.existsByName(request.getName())) {
            log.warn("하객 등록 실패, 이미 등록된 이름: {}", request.getName());
            throw new CustomException(ErrorCode.DUPLICATE_GUEST_NAME);
        }

        String token = UUID.randomUUID().toString();

        Guest guest = Guest.builder()
                .name(request.getName())
                .side(request.getSide())
                .token(token)
                .build();

        Guest saved = guestRepository.save(guest);

        return toResponse(saved);
    }

    @Transactional
    public List<GuestCreateResponse> deactivate(List<Long> guestIds) {
        if (guestIds == null || guestIds.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        return guestIds.stream()
                .map(this::deactivateOne)
                .toList();
    }

    private GuestCreateResponse deactivateOne(Long guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTITY_NOT_FOUND));

        guest.deactivate();

        return toResponse(guest);
    }

    private GuestCreateResponse toResponse(Guest guest) {
        return new GuestCreateResponse(
                guest.getId(),
                guest.getName(),
                guest.getSide(),
                guest.getToken(),
                guest.getIsActive(),
                guest.getCreatedAt()
        );
    }
}