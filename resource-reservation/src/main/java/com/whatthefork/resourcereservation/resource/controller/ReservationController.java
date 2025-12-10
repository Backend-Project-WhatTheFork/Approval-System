package com.whatthefork.resourcereservation.resource.controller;

import com.whatthefork.resourcereservation.common.ApiResponse;
import com.whatthefork.resourcereservation.resource.dto.request.create.CreateReservationRequest;
import com.whatthefork.resourcereservation.resource.dto.request.update.UpdateReservationRequest;
import com.whatthefork.resourcereservation.resource.dto.user.UserDetailsImpl;
import com.whatthefork.resourcereservation.resource.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Reservation", description = "예약 관리 API(예약 확인, 생성, 수정, 취소, 만료 예약 확인, 취소 예약 확인")
@RestController
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

    @Operation(summary = "예약 목록 확인", description = "존재하는 예약 목록을 전부 반환합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse> getAllReservations() {

        return ResponseEntity.ok(ApiResponse.success(reservationService.getAllReservations()));
    }

    @Operation(summary = "예약 생성", description = "예약을 생성하고 생성된 예약을 반환합니다.")
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

    @Operation(summary = "예약 취소", description = "예약을 취소하고 취소한 예약을 예약 취소 테이블에 추가한 뒤 해당 예약을 반환합니다.")
    @PostMapping("/cancellations/{reservationId}")
    @PreAuthorize("hasRole('ADMIN') or @reservationSecurity.isReservationOwner(principal.username, #reservationId)")
    public ResponseEntity<ApiResponse> cancelReservation(@PathVariable Long reservationId) {

        String currentUserId = getUserId();
        Long userId = Long.parseLong(currentUserId);

        return ResponseEntity.ok(ApiResponse.success(reservationService.cancelReservation(reservationId, userId)));
    }

    @Operation(summary = "예약 수정", description = "해당 예약을 만든 사용자인지 확인 후 예약을 수정 후 반환합니다.")
    @PatchMapping("/edit/{reservationId}")
    @PreAuthorize("hasRole('ADMIN') or @reservationSecurity.isReservationOwner(principal.username, #reservationId)")
    public ResponseEntity<ApiResponse> editReservation(@PathVariable Long reservationId, @RequestBody UpdateReservationRequest reservationRequest) {

        return ResponseEntity.ok(ApiResponse.success(reservationService.editReservation(reservationRequest, reservationId)));
    }

    @Operation(summary = "만료 예약 확인", description = "사용자의 모든 만료된 예약츨 반환합니다.")
    @GetMapping("/expired")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> expiredReservations() {

        String currentUserId = getUserId();
        Long userId = Long.parseLong(currentUserId);

        return ResponseEntity.ok(ApiResponse.success(reservationService.getExpiredReservations(userId)));
    }

    @Operation(summary = "취소 예약 확인", description = "사용자의 취소된 예약을 반환합니다.")
    @GetMapping("/canceled")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> canceledReservations() {

        String currentUserId = getUserId();
        Long userId = Long.parseLong(currentUserId);

        return ResponseEntity.ok(ApiResponse.success(reservationService.getCanceledReservations(userId)));
    }
}
