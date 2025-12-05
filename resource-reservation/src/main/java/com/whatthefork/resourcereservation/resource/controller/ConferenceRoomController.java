package com.whatthefork.resourcereservation.resource.controller;

import com.whatthefork.resourcereservation.common.ApiResponse;
import com.whatthefork.resourcereservation.resource.entity.Resources;
import com.whatthefork.resourcereservation.resource.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/v1/conference-rooms")
@RequiredArgsConstructor
public class ConferenceRoomController {

    private final ResourceService resourceService;

    @GetMapping("")
    public ResponseEntity<ApiResponse> conferenceRoom() {
        System.out.println("요청 들어옴");
        List<Resources> conferenceRoomList = new ArrayList<>();

        conferenceRoomList = resourceService.getAllConferenceRooms();

        return ResponseEntity.ok(ApiResponse.success(conferenceRoomList));
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> createConferenceRoom(@RequestBody Resources conferenceRoom) {


    }
}
