//package com.whatthefork.communicationandalarm.alarm.controller;
//
//import com.whatthefork.communicationandalarm.alarm.domain.AlarmService;
//import com.whatthefork.communicationandalarm.common.ApiResponse;
//import com.whatthefork.communicationandalarm.common.dto.request.CreatePostRequest;
//import com.whatthefork.communicationandalarm.post.domain.PostService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@Tag(name = "Alarm", description = "알림")
//public class AlarmController {
//
//    private final AlarmService alarmService;
//
//    @Operation(summary = "알림 등록", description = "새 알림을 등록합니다. ")
//    @PostMapping
//    public ResponseEntity<ApiResponse<Void>> createAlarm (
//            @PathVariable Long memberId,
//            @RequestParam Long memberId,
//            @RequestParam String memberName,
//            @RequestBody @Valid CreatePostRequest request
//    ) {
//        alaramService.create(memberId, memberName, request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
//    }
//}
