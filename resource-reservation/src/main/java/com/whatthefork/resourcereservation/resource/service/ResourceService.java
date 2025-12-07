package com.whatthefork.resourcereservation.resource.service;

import com.whatthefork.resourcereservation.exception.BusinessException;
import com.whatthefork.resourcereservation.exception.ErrorCode;
import com.whatthefork.resourcereservation.exception.GlobalExceptionHandler;
import com.whatthefork.resourcereservation.resource.dto.request.ResourceRequest;
import com.whatthefork.resourcereservation.resource.dto.response.ResourceResponse;
import com.whatthefork.resourcereservation.resource.entity.Resources;
import com.whatthefork.resourcereservation.resource.enums.ResourceCategory;
import com.whatthefork.resourcereservation.resource.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public ResourceRequest createResource(ResourceRequest resourceRequest) {

        return new ResourceRequest(resourceRepository.save(
                Resources.builder()
                .name(resourceRequest.name())
                .maxCapacity(resourceRequest.maxCapacity())
                .category(resourceRequest.category())
                .build()
        ));
    }

    public void deleteResource(Long id) {

        resourceRepository.deleteById(id);
    }

    public ResourceRequest updateResourceById(Long id, ResourceRequest resourceRequest) {

        Resources target = resourceRepository.findById(id)
                        .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        target.updateAll(resourceRequest);

        return new ResourceRequest(resourceRepository.save(target));
    }

    public ResourceRequest getResourceById(Long id) {

        return new ResourceRequest(resourceRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND))
        );
    }
}
