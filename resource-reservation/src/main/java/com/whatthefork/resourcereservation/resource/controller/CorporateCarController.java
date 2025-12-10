package com.whatthefork.resourcereservation.resource.controller;

import com.whatthefork.resourcereservation.common.ApiResponse;
import com.whatthefork.resourcereservation.resource.dto.request.create.CreateCorporateCarRequest;
import com.whatthefork.resourcereservation.resource.dto.request.update.UpdateCorporateCarRequest;
import com.whatthefork.resourcereservation.resource.dto.response.CorporateCarResponse;
import com.whatthefork.resourcereservation.resource.service.CorporateCarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "CorporateCars", description = "법인차량 관리 API(법인차량 조회, 추가, 삭제, 수정, 검색")
@Slf4j
@RestController
@RequestMapping("/corporate-cars")
@RequiredArgsConstructor
public class CorporateCarController {

    private final CorporateCarService corporateCarService;

    @Operation(summary = "법인차량 전체 조회", description = "등록되어 있는 법인차량을 전부 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse> getCorporateCars() {

        List<CorporateCarResponse> corporateCarsList = corporateCarService.getAllCorporateCars();

        log.info("Get corporate cars : {} ",  corporateCarsList);

        return ResponseEntity.ok(ApiResponse.success(corporateCarsList));
    }

    @Operation(summary = "법인차량 추가", description = "관리자의 권한으로 법인차량을 추가합니다. 추가된 법인차량의 DTO를 반환합니다. ")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createCorporateCar(@RequestBody CreateCorporateCarRequest corporateCarRequest) {
        log.info("create corporate car: {}", corporateCarRequest);

        return ResponseEntity.ok(ApiResponse.success(corporateCarService.createCorporateCar(corporateCarRequest)));
    }

    @Operation(summary = "법인차량 삭제", description = "관리자의 권한으로 법인차량을 삭제합니다. 삭제된 법인차량의 DTO를 반환합니다. ")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteCorporateCar(@PathVariable Long id) {
        log.info("delete corporate car: {}", id);

        corporateCarService.deleteCorporateCar(id);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "법인차량 추가", description = "관리자의 권한으로 법인차량을 수정합니다. 수정된 법인차량의 DTO를 반환합니다. ")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateCorporateCar(@PathVariable Long id, @RequestBody UpdateCorporateCarRequest corporateCarRequest) {
        log.info("update corporate car: {}", corporateCarRequest);

        return ResponseEntity.ok(ApiResponse.success(corporateCarService.updateCorporateCarById(id, corporateCarRequest)));
    }

    @Operation(summary = "법인차량 이름으로 조회", description = "법인차량 이름으로 목록을 조회합니다. 검색된 법인차량을 반환합니다.")
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getCorporateCarByName(@PathVariable String name) {
        log.info("get corporate car by name : {}", name);

        return ResponseEntity.ok(ApiResponse.success(corporateCarService.getCorporateCarByName(name)));
    }

    @Operation(summary = "법인차량 최대인원 조회", description = "최대 인원으로 법인차량을 조회합니다. 검색된 목록을 반환합니다.")
    @GetMapping("/maxCapacity/{maxCapacity}")
    public ResponseEntity<ApiResponse> getCorporateCarByMaxCapacity(@PathVariable int maxCapacity) {
        log.info("get corporate car by maxCapacity : {}", maxCapacity);

        return ResponseEntity.ok(ApiResponse.success(corporateCarService.getCorporateCarByMaxCapacity(maxCapacity)));
    }
}
