package com.whatthefork.resourcereservation.resource.repository;

import com.whatthefork.resourcereservation.resource.entity.Resources;
import com.whatthefork.resourcereservation.resource.enums.ResourceCategory;

import java.util.List;

public interface ResourceRepository {

    List<Resources> findByCategory(ResourceCategory category);
}
