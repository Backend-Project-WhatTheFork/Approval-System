package com.whatthefork.resourcereservation.resource.repository;

import com.whatthefork.resourcereservation.resource.dto.request.update.UpdateReservationRequest;
import com.whatthefork.resourcereservation.resource.entity.Reservation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository {

    List<Reservation> findAll();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Reservation save(Reservation reservation);

    void deleteById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Reservation> findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Reservation> findAllByUserId(Long userId);

    Optional<Reservation> findByUserId(Long userId);
}
