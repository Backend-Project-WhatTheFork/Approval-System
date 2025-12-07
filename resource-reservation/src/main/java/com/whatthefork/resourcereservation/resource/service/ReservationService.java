package com.whatthefork.resourcereservation.resource.service;

import com.whatthefork.resourcereservation.exception.BusinessException;
import com.whatthefork.resourcereservation.exception.ErrorCode;
import com.whatthefork.resourcereservation.resource.dto.request.create.CreateReservationRequest;
import com.whatthefork.resourcereservation.resource.dto.response.ConferenceRoomResponse;
import com.whatthefork.resourcereservation.resource.dto.response.CorporateCarResponse;
import com.whatthefork.resourcereservation.resource.dto.response.ReservationAndConferenceRoom;
import com.whatthefork.resourcereservation.resource.dto.response.ReservationAndCorporateCar;
import com.whatthefork.resourcereservation.resource.dto.response.ReservationResponse;
import com.whatthefork.resourcereservation.resource.entity.Reservation;
import com.whatthefork.resourcereservation.resource.repository.ConferenceRoomRepository;
import com.whatthefork.resourcereservation.resource.repository.CorporateCarRepository;
import com.whatthefork.resourcereservation.resource.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ConferenceRoomRepository conferenceRoomRepository;
    private final CorporateCarRepository corporateCarRepository;

    public List<ReservationResponse> getAllReservations() {

        return reservationRepository.findAll().stream()
                .map(reservation -> { return new ReservationResponse(reservation); })
                .collect(Collectors.toList());
    }

    public ReservationAndConferenceRoom createRoomReservation(CreateReservationRequest reservationRequest, Long userId) {

        ReservationResponse response = new ReservationResponse(reservationRepository.save(
                Reservation.builder()
                        .userId(userId)
                        .resourceId(reservationRequest.resourceId())
                        .bookedDate(reservationRequest.bookedDate())
                        .startDate(reservationRequest.startDate())
                        .endDate(reservationRequest.endDate())
                        .capacity(reservationRequest.capacity())
                        .reason(reservationRequest.reason())
                        .category(reservationRequest.category())
                        .build()
        ));

        ConferenceRoomResponse conferenceRoomResponse = new ConferenceRoomResponse(conferenceRoomRepository
                .findById(reservationRequest.resourceId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND)));

        ReservationAndConferenceRoom reservationAndConferenceRoom =
                new ReservationAndConferenceRoom(response, conferenceRoomResponse);

        return reservationAndConferenceRoom;
    }

    public ReservationAndCorporateCar createVehicleReservation(CreateReservationRequest reservationRequest, Long userId) {

        ReservationResponse response = new ReservationResponse(reservationRepository.save(
                Reservation.builder()
                        .userId(userId)
                        .resourceId(reservationRequest.resourceId())
                        .bookedDate(reservationRequest.bookedDate())
                        .startDate(reservationRequest.startDate())
                        .endDate(reservationRequest.endDate())
                        .capacity(reservationRequest.capacity())
                        .reason(reservationRequest.reason())
                        .category(reservationRequest.category())
                        .build()
        ));

        CorporateCarResponse corporateCarResponse = new CorporateCarResponse(corporateCarRepository
                .findById(reservationRequest.resourceId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND)));

        ReservationAndCorporateCar reservationAndCorporateCar =
                new ReservationAndCorporateCar(response, corporateCarResponse);

        return reservationAndCorporateCar;
    }

}
