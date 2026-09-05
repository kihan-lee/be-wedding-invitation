package com.wedding.invitation.admin.controller;

import com.wedding.invitation.admin.dto.DashboardResponse;
import com.wedding.invitation.admin.dto.GuestCreateRequest;
import com.wedding.invitation.admin.dto.GuestCreateResponse;
import com.wedding.invitation.admin.dto.LoginRequest;
import com.wedding.invitation.admin.dto.LoginResponse;
import com.wedding.invitation.admin.dto.MessageResponse;
import com.wedding.invitation.admin.dto.RecentActivityResponse;
import com.wedding.invitation.admin.service.AuthService;
import com.wedding.invitation.admin.service.DashboardService;
import com.wedding.invitation.admin.service.GuestService;
import com.wedding.invitation.admin.service.MessageService;
import com.wedding.invitation.common.response.ApiResponse;
import com.wedding.invitation.domain.Guest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@Validated
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final DashboardService dashboardService;
    private final GuestService guestService;
    private final MessageService messageService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ApiResponse.success(response);
    }

    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse> getDashboard() {
        DashboardResponse response = dashboardService.getDashboard();
        return ApiResponse.success(response);
    }

    @GetMapping("/dashboard/recent")
    public ApiResponse<RecentActivityResponse> getRecentActivity() {
        RecentActivityResponse response = dashboardService.getRecentActivity();
        return ApiResponse.success(response);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/guests")
    public ApiResponse<List<GuestCreateResponse>> createGuests(
            @RequestBody List<@Valid GuestCreateRequest> requests
    ) {
        List<GuestCreateResponse> response = guestService.create(requests);
        return ApiResponse.success(response);
    }

    @GetMapping("/guests")
    public ApiResponse<List<GuestCreateResponse>> getGuests(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Guest.Side side
    ) {
        List<GuestCreateResponse> response = guestService.getGuests(active, side);
        return ApiResponse.success(response);
    }

    @PatchMapping("/guests/deactivate")
    public ApiResponse<List<GuestCreateResponse>> deactivateGuests(@RequestBody List<Long> guestIds) {
        List<GuestCreateResponse> response = guestService.deactivate(guestIds);
        return ApiResponse.success(response);
    }

    @GetMapping("/messages")
    public ApiResponse<List<MessageResponse>> getMessages() {
        List<MessageResponse> response = messageService.getMessages();
        return ApiResponse.success(response);
    }
}