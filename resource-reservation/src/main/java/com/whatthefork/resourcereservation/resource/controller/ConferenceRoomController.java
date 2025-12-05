package com.whatthefork.resourcereservation.resource.controller;

import com.whatthefork.resourcereservation.common.ApiResponse;
import com.whatthefork.resourcereservation.resource.entity.Resources;
import com.whatthefork.resourcereservation.resource.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/vi/conference-room")
@RequiredArgsConstructor
public class ConferenceRoomController {

    private final ResourceService resourceService;

    @GetMapping("/")
    public ResponseEntity<ApiResponse> conferenceRoom() {

        List<Resources> conferenceRoomList = new ArrayList<>();

        conferenceRoomList = resourceService.getAllConferenceRooms();

        return conferenceRoomList;
    }
}
