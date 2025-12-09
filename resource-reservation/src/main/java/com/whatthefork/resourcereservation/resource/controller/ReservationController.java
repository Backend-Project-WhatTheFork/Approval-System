package com.whatthefork.resourcereservation.resource.controller;

import com.whatthefork.resourcereservation.common.ApiResponse;
import com.whatthefork.resourcereservation.resource.dto.request.create.CreateReservationRequest;
import com.whatthefork.resourcereservation.resource.dto.request.update.UpdateReservationRequest;
import com.whatthefork.resourcereservation.resource.dto.user.UserDetailsImpl;
import com.whatthefork.resourcereservation.resource.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/v1/reservations")
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;

        return userDetails.getUsername();
    }

    // 예약 목록 확인
    @GetMapping
    public ResponseEntity<ApiResponse> getAllReservations() {

        return ResponseEntity.ok(ApiResponse.success(reservationService.getAllReservations()));
    }

    // 예약 생성
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createReservation
    (@RequestBody CreateReservationRequest reservationRequest) {

        String userId = getUserId();

        Long castedUserId = Long.parseLong(userId);

        switch(reservationRequest.category()) {
            case CONFERENCE_ROOM -> { return ResponseEntity.ok(ApiResponse.success(reservationService.createRoomReservation(reservationRequest, castedUserId))); }
            case CORPORATE_VEHICLE -> { return ResponseEntity.ok(ApiResponse.success(reservationService.createVehicleReservation(reservationRequest, castedUserId))); }
            case SUPPLIES -> { return ResponseEntity.ok(ApiResponse.success(reservationService.createSupplyReservation(reservationRequest, castedUserId))); }
            default -> { return null; }
        }
    }

    // 예약 취소
    @PostMapping("/cancellations/{reservationId}")
    @PreAuthorize("hasRole('ADMIN') or @reservationSecurity.isReservationOwner(principal.username, #reservationId)")
    public ResponseEntity<ApiResponse> cancelReservation(@PathVariable Long reservationId) {

        String currentUserId = getUserId();

        Long userId = Long.parseLong(currentUserId);

        return ResponseEntity.ok(ApiResponse.success(reservationService.cancelReservation(reservationId, userId)));
    }

    // 예약 수정
    @PatchMapping("/edit/{reservationId}")
    @PreAuthorize("hasRole('ADMIN') or @reservationSecurity.isReservationOwner(principal.username, #reservationId)")
    public ResponseEntity<ApiResponse> editReservation(@PathVariable Long reservationId, @RequestBody UpdateReservationRequest reservationRequest) {

        return ResponseEntity.ok(ApiResponse.success(reservationService.editReservation(reservationRequest, reservationId)));
    }

    // 내 만료 예약 목록 확인
    @GetMapping("/expired")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> expiredReservations() {

        String currentUserId = getUserId();

        Long userId = Long.parseLong(currentUserId);

        return ResponseEntity.ok(ApiResponse.success(reservationService.getExpiredReservations(userId)));
    }

    // 내 취소 예약 목록 확인
    @GetMapping("/canceled")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> canceledReservations() {

        String currentUserId = getUserId();

        Long userId = Long.parseLong(currentUserId);

        return ResponseEntity.ok(ApiResponse.success(reservationService.getCanceledReservations(userId)));
    }
}
