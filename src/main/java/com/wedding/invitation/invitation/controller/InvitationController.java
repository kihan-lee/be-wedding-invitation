package com.wedding.invitation.invitation.controller;

import com.wedding.invitation.common.response.ApiResponse;
import com.wedding.invitation.domain.AccessType;
import com.wedding.invitation.invitation.dto.InvitationResponse;
import com.wedding.invitation.invitation.dto.MessageCreateRequest;
import com.wedding.invitation.invitation.service.InvitationMessageService;
import com.wedding.invitation.invitation.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invitation")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;
    private final InvitationMessageService messageService;

    @GetMapping
    public ApiResponse<InvitationResponse> getInvitation(
            @RequestParam(required = false) String token,
            @RequestParam AccessType accessType
    ) {
        InvitationResponse response = invitationService.getInvitation(token, accessType);
        return ApiResponse.success(response);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/message")
    public ApiResponse<Void> createMessage(@Valid @RequestBody MessageCreateRequest request) {
        messageService.create(request);
        return ApiResponse.success();
    }
}