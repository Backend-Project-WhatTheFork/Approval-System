package com.whatthefork.resourcereservation.resource.service;

import com.whatthefork.resourcereservation.resource.dto.response.ResourceResponse;
import com.whatthefork.resourcereservation.resource.entity.Resources;
import com.whatthefork.resourcereservation.resource.enums.ResourceCategory;
import com.whatthefork.resourcereservation.resource.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public List<ResourceResponse> getAllConferenceRooms() {

        List<Resources> conferenceRoomEntityList = new ArrayList<>();

        conferenceRoomEntityList = resourceRepository.findAllByCategory(ResourceCategory.CONFERENCE_ROOM);

        return conferenceRoomEntityList.stream()
                .map(resource -> {return new ResourceResponse(resource);})
                .collect(Collectors.toList());
    }
}
