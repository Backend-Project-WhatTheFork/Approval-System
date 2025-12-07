package com.whatthefork.resourcereservation.resource.repository;

import com.whatthefork.resourcereservation.resource.entity.CanceledReservation;
import com.whatthefork.resourcereservation.resource.entity.Reservation;

public interface CanceledReservationRepository {
    CanceledReservation save(CanceledReservation reservation);
}
