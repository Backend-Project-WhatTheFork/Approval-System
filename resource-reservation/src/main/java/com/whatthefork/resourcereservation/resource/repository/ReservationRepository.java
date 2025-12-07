package com.whatthefork.resourcereservation.resource.repository;

import com.whatthefork.resourcereservation.resource.entity.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    Optional<Reservation> findById(Long id);
}
