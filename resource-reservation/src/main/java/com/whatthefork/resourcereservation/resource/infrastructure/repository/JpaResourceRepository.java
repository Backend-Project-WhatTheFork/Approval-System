package com.whatthefork.resourcereservation.resource.infrastructure.repository;

import com.whatthefork.resourcereservation.resource.entity.Resources;
import com.whatthefork.resourcereservation.resource.repository.ResourceRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaResourceRepository extends ResourceRepository, JpaRepository<Resources, Long> {
}
