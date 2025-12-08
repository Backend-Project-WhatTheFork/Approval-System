package com.whatthefork.resourcereservation.resource.repository;

import com.whatthefork.resourcereservation.resource.entity.ConferenceRoom;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConferenceRoomRepository {

    ConferenceRoom save(ConferenceRoom resource);

    void deleteById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ConferenceRoom> findById(Long id);

    Optional<ConferenceRoom> findByName(String name);

    ConferenceRoom findByMaxCapacity(int maxCapacity);

    List<ConferenceRoom> findAll();
}
