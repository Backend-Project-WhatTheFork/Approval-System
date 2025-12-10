package com.whatthefork.attendancetracking.annualLeave.controller;

import com.whatthefork.attendancetracking.annualLeave.dto.AnnualLeaveHistoryResponse;
import com.whatthefork.attendancetracking.annualLeave.dto.AnnualLeaveResponse;
import com.whatthefork.attendancetracking.annualLeave.service.AnnualLeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.whatthefork.attendancetracking.common.ApiResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/annualLeave")
public class AnnualLeaveController {

    private final AnnualLeaveService annualLeaveService;

    //연차 결재 현황 확인(년도별)
    @GetMapping("/{year}")
    public ResponseEntity<ApiResponse> getAnnualLeaveSummary(
            @AuthenticationPrincipal String memberIds,
            @PathVariable Integer year
    ) {
        Long memberId =  Long.parseLong(memberIds);

        Optional<AnnualLeaveResponse> response = annualLeaveService.getAnnualLeave(memberId, year);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


    //연차 결재 히스토리 조회
    @GetMapping("/{year}/histories")
    public ResponseEntity<ApiResponse> getAnnualLeaveHistories(
            @AuthenticationPrincipal String memberIds,
            @PathVariable Integer year
    ) {
        Long memberId =  Long.parseLong(memberIds);

        List<AnnualLeaveHistoryResponse> responses =
                annualLeaveService.getAnnualLeaveHistories(memberId, year);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }


}