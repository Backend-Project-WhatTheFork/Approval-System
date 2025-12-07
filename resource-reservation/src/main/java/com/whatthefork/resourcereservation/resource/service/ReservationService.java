package com.whatthefork.resourcereservation.resource.service;

import com.whatthefork.resourcereservation.exception.BusinessException;
import com.whatthefork.resourcereservation.exception.ErrorCode;
import com.whatthefork.resourcereservation.resource.dto.request.create.CreateReservationRequest;
import com.whatthefork.resourcereservation.resource.dto.response.CanceledReservationResponse;
import com.whatthefork.resourcereservation.resource.dto.response.ConferenceRoomResponse;
import com.whatthefork.resourcereservation.resource.dto.response.CorporateCarResponse;
import com.whatthefork.resourcereservation.resource.dto.response.ReservationAndConferenceRoom;
import com.whatthefork.resourcereservation.resource.dto.response.ReservationAndCorporateCar;
import com.whatthefork.resourcereservation.resource.dto.response.ReservationAndSupply;
import com.whatthefork.resourcereservation.resource.dto.response.ReservationResponse;
import com.whatthefork.resourcereservation.resource.dto.response.SuppliesResponse;
import com.whatthefork.resourcereservation.resource.entity.CanceledReservation;
import com.whatthefork.resourcereservation.resource.entity.ConferenceRoom;
import com.whatthefork.resourcereservation.resource.entity.CorporateCar;
import com.whatthefork.resourcereservation.resource.entity.Reservation;
import com.whatthefork.resourcereservation.resource.entity.Supplies;
import com.whatthefork.resourcereservation.resource.repository.CanceledReservationRepository;
import com.whatthefork.resourcereservation.resource.repository.ConferenceRoomRepository;
import com.whatthefork.resourcereservation.resource.repository.CorporateCarRepository;
import com.whatthefork.resourcereservation.resource.repository.ReservationRepository;
import com.whatthefork.resourcereservation.resource.repository.SupplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ConferenceRoomRepository conferenceRoomRepository;
    private final CorporateCarRepository corporateCarRepository;
    private final SupplyRepository supplyRepository;
    private final CanceledReservationRepository cancelRepository;

    public List<ReservationResponse> getAllReservations() {

        return reservationRepository.findAll().stream()
                .map(reservation -> { return new ReservationResponse(reservation); })
                .collect(Collectors.toList());
    }

    // 아래 3개 그지같은 코드 리팩토링 예정
    @Transactional
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

        ConferenceRoom conferenceRoom = conferenceRoomRepository.findById(reservationRequest.resourceId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        if (conferenceRoom.isBooked()) {
            throw new BusinessException(ErrorCode.REDUNDANT_RESERVATION);
        }

        conferenceRoom.updateIsBooked(true);
        conferenceRoomRepository.save(conferenceRoom);

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

        CorporateCar corporateCar = corporateCarRepository.findById(reservationRequest.resourceId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        if (corporateCar.isBooked()) {
            throw new BusinessException(ErrorCode.REDUNDANT_RESERVATION);
        }

        corporateCar.updateIsBooked(true);
        corporateCarRepository.save(corporateCar);

        CorporateCarResponse corporateCarResponse = new CorporateCarResponse(corporateCarRepository
                .findById(reservationRequest.resourceId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND)));

        ReservationAndCorporateCar reservationAndCorporateCar =
                new ReservationAndCorporateCar(response, corporateCarResponse);

        return reservationAndCorporateCar;
    }

    public ReservationAndSupply createSupplyReservation(CreateReservationRequest reservationRequest, Long userId) {

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

        Supplies supply = supplyRepository.findById(reservationRequest.resourceId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        if (supply.isBooked()) {
            throw new BusinessException(ErrorCode.REDUNDANT_RESERVATION);
        }

        supply.updateIsBooked(true);
        supplyRepository.save(supply);

        SuppliesResponse suppliesResponse = new SuppliesResponse(supplyRepository
                .findById(reservationRequest.resourceId())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND)));

        ReservationAndSupply reservationAndSupply =
                new ReservationAndSupply(response, suppliesResponse);

        return reservationAndSupply;
    }

    public CanceledReservationResponse cancelReservation(Long id) {

        // 취소할 예약 가져오기
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));

        // 예약 취소 테이블에 데이터 추가
        CanceledReservation canceledReservation = cancelRepository.save(CanceledReservation.builder()
                        .reason(reservation.getReason())
                        .canceledDate(LocalDateTime.now())
                        .build()
                );

        // 잡혀 있던 자원 상태 예약 가능으로 바꾸기
        switch (reservation.getCategory()) {
            case CONFERENCE_ROOM -> {
                conferenceRoomRepository.save(
                        conferenceRoomRepository.findById(reservation.getResourceId())
                                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND))
                                .updateIsBooked(false)
                );
            }
            case CORPORATE_VEHICLE -> {
                corporateCarRepository.save(
                        corporateCarRepository.findById(reservation.getResourceId())
                                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND))
                                .updateIsBooked(false)
                );
            }
            case SUPPLIES -> {
                supplyRepository.save(
                        supplyRepository.findById(reservation.getResourceId())
                                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND))
                                .updateIsBooked(false)
                );
            }
        }

        // 있던 예약 삭제
        reservationRepository.deleteById(id);

        return new CanceledReservationResponse(canceledReservation);
    }
}
