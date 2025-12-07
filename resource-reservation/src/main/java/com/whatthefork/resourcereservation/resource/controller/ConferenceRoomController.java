package com.whatthefork.resourcereservation.resource.controller;

import com.whatthefork.resourcereservation.common.ApiResponse;
import com.whatthefork.resourcereservation.resource.dto.request.ResourceRequest;
import com.whatthefork.resourcereservation.resource.dto.response.ResourceResponse;
import com.whatthefork.resourcereservation.resource.entity.Resources;
import com.whatthefork.resourcereservation.resource.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conference-rooms")
@RequiredArgsConstructor
public class ConferenceRoomController {

    private final ResourceService resourceService;

    @GetMapping
    public ResponseEntity<ApiResponse> conferenceRoom() {
        System.out.println("요청 들어옴");

        List<ResourceResponse> conferenceRoomList = resourceService.getAllConferenceRooms();

        return ResponseEntity.ok(ApiResponse.success(conferenceRoomList));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createConferenceRoom(@RequestBody ResourceRequest conferenceRoom) {
        System.out.println("회의실 생성 요청 들어옴");

        return ResponseEntity.ok(ApiResponse.success(resourceService.createResource(conferenceRoom)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteConferenceRoom(@PathVariable Long id) {
        System.out.println("회의실 삭제 요청 들어옴");

        resourceService.deleteResource(id);

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateConferenceRoom(@PathVariable Long id, @RequestBody ResourceRequest resources) {
        System.out.println("회의실 변경 요청 들어옴");

        return ResponseEntity.ok(ApiResponse.success(resourceService.updateResourceById(id, resources)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getConferenceRoomById(@PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.success(resourceService.getResourceById(id)));
    }
}
