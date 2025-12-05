package com.whatthefork.resourcereservation.resource.service;

import com.whatthefork.resourcereservation.resource.entity.Resources;
import com.whatthefork.resourcereservation.resource.enums.ResourceCategory;
import com.whatthefork.resourcereservation.resource.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public List<Resources> getAllConferenceRooms() {

        List<Resources> conferenceRoomList = new ArrayList<>();

        conferenceRoomList = resourceRepository.findByCategory(ResourceCategory.CONFERENCE_ROOM);
        conferenceRoomList.stream().forEach(conferenceRoom -> {
            System.out.println(conferenceRoom);
        });
        return conferenceRoomList;
    }
}
