package com.whatthefork.resourcereservation.resource.controller;

import com.whatthefork.resourcereservation.common.ApiResponse;
import com.whatthefork.resourcereservation.resource.dto.request.create.CreateSuppliesRequest;
import com.whatthefork.resourcereservation.resource.dto.request.update.UpdateSuppliesRequest;
import com.whatthefork.resourcereservation.resource.dto.response.SuppliesResponse;
import com.whatthefork.resourcereservation.resource.service.SupplyService;
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

@Tag(name = "Supply", description = "비품 관리 API(비품 조회, 추가, 삭제, 수정, 검색")
@Slf4j
@RestController
//@RequestMapping("/api/v1/supplies")
@RequestMapping("/supplies")
@RequiredArgsConstructor
public class SuppliesController {

    private final SupplyService supplyService;

    @Operation(summary = "비품 전체 조회", description = "모든 비품 목록을 반환합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse> getSupplies() {

        List<SuppliesResponse> suppliesResponsesList = supplyService.getAllSupplies();

        log.info("get all supplies: {} ", suppliesResponsesList);

        return ResponseEntity.ok(ApiResponse.success(suppliesResponsesList));
    }

    @Operation(summary = "비품 추가", description = "관리자의 권한으로 비품을 추가합니다. 추가된 비품의 DTO를 반환합니다. ")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createSupply(@RequestBody CreateSuppliesRequest suppliesRequest) {
        log.info("create supplies Request: {}", suppliesRequest);

        return ResponseEntity.ok(ApiResponse.success(supplyService.createSupply(suppliesRequest)));
    }

    @Operation(summary = "비품 삭제", description = "관리자의 권한으로 비품을 삭제합니다. 삭제된 비품의 DTO를 반환합니다. ")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteSupply(@PathVariable Long id) {
        log.info("delete supply: {}", id);

        supplyService.deleteSupply(id);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "비품 수정", description = "관리자의 권한으로 비품을 수정합니다. 수정된 비품의 DTO를 반환합니다. ")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateSupply(@PathVariable Long id, @RequestBody UpdateSuppliesRequest suppliesRequest) {
        log.info("update supply: {}", id);

        return ResponseEntity.ok(ApiResponse.success(supplyService.updateSupplyById(id, suppliesRequest)));
    }

    @Operation(summary = "비품 이름 검색", description = "검색된 비품을 반환합니다.")
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getSupplyById(@PathVariable String name) {
        log.info("get supply by name: {}", name);

        return ResponseEntity.ok(ApiResponse.success(supplyService.getSupplyByName(name)));
    }

}