package com.whatthefork.resourcereservation.resource.repository;

import com.whatthefork.resourcereservation.resource.entity.Reservation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);
}
