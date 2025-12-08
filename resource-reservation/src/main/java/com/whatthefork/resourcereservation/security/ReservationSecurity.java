package com.whatthefork.resourcereservation.security;

import com.whatthefork.resourcereservation.resource.entity.Reservation;
import com.whatthefork.resourcereservation.resource.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("reservationSecurity") // @PreAuthorize에서 참조할 이름 지정
@RequiredArgsConstructor
public class ReservationSecurity {

    private final ReservationRepository reservationRepository;

    /**
     * 특정 예약의 소유자가 현재 사용자와 동일한지 확인합니다.
     */
    public boolean isReservationOwner(String currentUserId, Long reservationId) {
        // DB에서 예약 정보를 조회하여 소유자 ID를 비교합니다.

        // 주의: 이 메서드는 성능을 위해 단순화되었습니다. 실제로는 DTO를 사용하는 것이 좋습니다.
        return reservationRepository.findById(reservationId)
                .map(Reservation::getUserId) // 예약 엔티티에서 소유자 ID를 가져옴
                .map(ownerId -> ownerId.equals(currentUserId)) // 현재 사용자 ID와 비교
                .orElse(false); // 예약이 존재하지 않으면 접근 불가
    }
}