package com.whatthefork.resourcereservation.resource.repository;

import com.whatthefork.resourcereservation.resource.dto.request.ResourceRequest;
import com.whatthefork.resourcereservation.resource.entity.Resources;
import com.whatthefork.resourcereservation.resource.enums.ResourceCategory;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository {

    List<Resources> findAllByCategory(ResourceCategory category);

    Resources save(Resources resource);

    void deleteById(Long id);

    Optional<Resources> findById(Long id);

    Optional<Resources> findByName(String name);

    Resources findByMaxCapacityAndCategory(int maxCapacity, ResourceCategory resourceCategory);
}
