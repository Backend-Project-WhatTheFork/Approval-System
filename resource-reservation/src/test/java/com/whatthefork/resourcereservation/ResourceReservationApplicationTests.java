package com.whatthefork.resourcereservation;

import com.whatthefork.resourcereservation.common.ApiResponse;
import com.whatthefork.resourcereservation.resource.controller.ReservationController;
import com.whatthefork.resourcereservation.resource.dto.request.create.CreateReservationRequest;
import com.whatthefork.resourcereservation.resource.dto.response.ReservationAndConferenceRoom;
import com.whatthefork.resourcereservation.resource.dto.response.ReservationAndCorporateCar; // New Import
import com.whatthefork.resourcereservation.resource.dto.response.ReservationAndSupply; // New Import
import com.whatthefork.resourcereservation.resource.dto.response.ReservationResponse;
import com.whatthefork.resourcereservation.resource.dto.response.ConferenceRoomResponse;
import com.whatthefork.resourcereservation.resource.dto.response.CorporateCarResponse; // New Import
import com.whatthefork.resourcereservation.resource.dto.response.SuppliesResponse; // New Import
import com.whatthefork.resourcereservation.resource.service.ReservationService;
import com.whatthefork.resourcereservation.resource.enums.ResourceCategory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("ReservationController 단위 테스트 (CreateReservation)")
class ReservationControllerUnitTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private AutoCloseable closeable;

    private final String MOCK_USER_NAME = "20251210";
    private final Long MOCK_USER_ID = 20251210L;
    private final LocalDateTime NOW = LocalDateTime.now();
    private final LocalDateTime FUTURE_START = NOW.plusDays(1);
    private final LocalDateTime FUTURE_END = NOW.plusDays(2);


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        closeable.close();
    }

    private void setAuthenticatedUser(String username) {
        UserDetails userDetails = new User(username, "dummy",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private CreateReservationRequest createMockRequest(ResourceCategory category) {
        return new CreateReservationRequest(
                1L,
                NOW.minusHours(1),
                FUTURE_START,
                FUTURE_END,
                2,
                "테스트 사유",
                category
        );
    }

    // --- 1. 회의실 예약 테스트 (기존 유지) ---

    @Test
    @DisplayName("회의실 예약 시 createRoomReservation이 호출되고 ReservationAndConferenceRoom을 반환해야 한다")
    void createReservation_ConferenceRoom_ShouldCallRoomService() {
        // Given
        setAuthenticatedUser(MOCK_USER_NAME);
        CreateReservationRequest request = createMockRequest(ResourceCategory.CONFERENCE_ROOM);

        // Mock 객체 생성
        ReservationAndConferenceRoom expectedResponseDto = new ReservationAndConferenceRoom(
                mock(ReservationResponse.class),
                mock(ConferenceRoomResponse.class)
        );

        given(reservationService.createRoomReservation(any(CreateReservationRequest.class), eq(MOCK_USER_ID)))
                .willReturn(expectedResponseDto);

        // When
        ResponseEntity<ApiResponse> response = reservationController.createReservation(request);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponseDto, response.getBody().getData());

        // Service 호출 검증
        verify(reservationService, times(1)).createRoomReservation(any(CreateReservationRequest.class), eq(MOCK_USER_ID));
        verify(reservationService, never()).createVehicleReservation(any(), any());
        verify(reservationService, never()).createSupplyReservation(any(), any());
    }

    // --- 2. 법인차량 예약 테스트 (CorporateCar 반영) ---

    @Test
    @DisplayName("법인차량 예약 시 createVehicleReservation이 호출되고 ReservationAndCorporateCar를 반환해야 한다")
    void createReservation_CorporateVehicle_ShouldCallVehicleService() {
        // Given
        setAuthenticatedUser(MOCK_USER_NAME);
        CreateReservationRequest request = createMockRequest(ResourceCategory.CORPORATE_VEHICLE);

        // 🚨 수정: ReservationAndCorporateCar DTO를 반환하도록 Mocking
        ReservationAndCorporateCar expectedResponseDto = new ReservationAndCorporateCar(
                mock(ReservationResponse.class),
                mock(CorporateCarResponse.class)
        );

        given(reservationService.createVehicleReservation(any(CreateReservationRequest.class), eq(MOCK_USER_ID)))
                .willReturn(expectedResponseDto);

        // When
        ResponseEntity<ApiResponse> response = reservationController.createReservation(request);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponseDto, response.getBody().getData());

        // Service 호출 검증
        verify(reservationService, times(1)).createVehicleReservation(any(CreateReservationRequest.class), eq(MOCK_USER_ID));
        verify(reservationService, never()).createRoomReservation(any(), any());
        verify(reservationService, never()).createSupplyReservation(any(), any());
    }

    // --- 3. 비품 예약 테스트 (Supplies 반영) ---

    @Test
    @DisplayName("비품 예약 시 createSupplyReservation이 호출되고 ReservationAndSupply를 반환해야 한다")
    void createReservation_Supplies_ShouldCallSupplyService() {
        // Given
        setAuthenticatedUser(MOCK_USER_NAME);
        CreateReservationRequest request = createMockRequest(ResourceCategory.SUPPLIES);

        // 🚨 수정: ReservationAndSupply DTO를 반환하도록 Mocking
        ReservationAndSupply expectedResponseDto = new ReservationAndSupply(
                mock(ReservationResponse.class),
                mock(SuppliesResponse.class)
        );

        given(reservationService.createSupplyReservation(any(CreateReservationRequest.class), eq(MOCK_USER_ID)))
                .willReturn(expectedResponseDto);

        // When
        ResponseEntity<ApiResponse> response = reservationController.createReservation(request);

        // Then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponseDto, response.getBody().getData());

        // Service 호출 검증
        verify(reservationService, times(1)).createSupplyReservation(any(CreateReservationRequest.class), eq(MOCK_USER_ID));
        verify(reservationService, never()).createRoomReservation(any(), any());
        verify(reservationService, never()).createVehicleReservation(any(), any());
    }
}