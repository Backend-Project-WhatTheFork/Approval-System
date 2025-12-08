package com.whatthefork.userservice.auth.service;

import com.whatthefork.userservice.auth.dto.LoginRequest;
import com.whatthefork.userservice.auth.dto.TokenResponse;
import com.whatthefork.userservice.auth.entity.RefreshToken;
import com.whatthefork.userservice.auth.repository.RefreshTokenRepository;
import com.whatthefork.userservice.command.entity.User;
import com.whatthefork.userservice.command.entity.UserRole;
import com.whatthefork.userservice.command.repository.UserRepository;
import com.whatthefork.userservice.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("encodedPassword")
                .positionCode(1)
                .deptId("DEPT001")
                .isDeptLeader(false)
                .isAdmin(false)
                .role(UserRole.USER)
                .build();

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    @DisplayName("올바른 이메일과 비밀번호로 로그인에 성공한다")
    void login_WithValidCredentials_ReturnsTokenResponse() {
        // given
        given(userRepository.findByEmail(loginRequest.getEmail())).willReturn(Optional.of(testUser));
        given(passwordEncoder.matches(loginRequest.getPassword(), testUser.getPassword())).willReturn(true);
        given(jwtTokenProvider.createToken(anyString(), anyString(), any())).willReturn("accessToken");
        given(jwtTokenProvider.createRefreshToken(anyString(), anyString(), any())).willReturn("refreshToken");
        given(jwtTokenProvider.getRefreshExpiration()).willReturn(604800000L);

        // when
        TokenResponse result = authService.login(loginRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo("accessToken");
        assertThat(result.getRefreshToken()).isEqualTo("refreshToken");
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), testUser.getPassword());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시 BadCredentialsException을 던진다")
    void login_WithNonExistentEmail_ThrowsBadCredentialsException() {
        // given
        given(userRepository.findByEmail(loginRequest.getEmail())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("올바르지 않은 아이디 혹은 비밀번호");
        verify(userRepository).findByEmail(loginRequest.getEmail());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시 BadCredentialsException을 던진다")
    void login_WithInvalidPassword_ThrowsBadCredentialsException() {
        // given
        given(userRepository.findByEmail(loginRequest.getEmail())).willReturn(Optional.of(testUser));
        given(passwordEncoder.matches(loginRequest.getPassword(), testUser.getPassword())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("올바르지 않은 아이디 혹은 비밀번호");
        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), testUser.getPassword());
    }

    @Test
    @DisplayName("유효한 리프레시 토큰으로 새로운 토큰을 발급한다")
    void refreshToken_WithValidRefreshToken_ReturnsNewTokenResponse() {
        // given
        String validRefreshToken = "validRefreshToken";
        String email = "test@example.com";

        RefreshToken storedToken = RefreshToken.builder()
                .username(email)
                .token(validRefreshToken)
                .expiryDate(new Date(System.currentTimeMillis() + 3600000))
                .build();

        given(jwtTokenProvider.getUsernameFromJWT(validRefreshToken)).willReturn(email);
        given(refreshTokenRepository.findById(email)).willReturn(Optional.of(storedToken));
        given(userRepository.findByEmail(email)).willReturn(Optional.of(testUser));
        given(jwtTokenProvider.createToken(anyString(), anyString(), any())).willReturn("newAccessToken");
        given(jwtTokenProvider.createRefreshToken(anyString(), anyString(), any())).willReturn("newRefreshToken");
        given(jwtTokenProvider.getRefreshExpiration()).willReturn(604800000L);

        // when
        TokenResponse result = authService.refreshToken(validRefreshToken);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo("newAccessToken");
        assertThat(result.getRefreshToken()).isEqualTo("newRefreshToken");
        verify(jwtTokenProvider).validateToken(validRefreshToken);
        verify(refreshTokenRepository).findById(email);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("저장된 리프레시 토큰과 일치하지 않으면 BadCredentialsException을 던진다")
    void refreshToken_WithMismatchedToken_ThrowsBadCredentialsException() {
        // given
        String providedRefreshToken = "providedRefreshToken";
        String storedRefreshToken = "storedRefreshToken";
        String email = "test@example.com";

        RefreshToken storedToken = RefreshToken.builder()
                .username(email)
                .token(storedRefreshToken)
                .expiryDate(new Date(System.currentTimeMillis() + 3600000))
                .build();

        given(jwtTokenProvider.getUsernameFromJWT(providedRefreshToken)).willReturn(email);
        given(refreshTokenRepository.findById(email)).willReturn(Optional.of(storedToken));

        // when & then
        assertThatThrownBy(() -> authService.refreshToken(providedRefreshToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("리프레시 토큰 일치하지 않음");
        verify(jwtTokenProvider).validateToken(providedRefreshToken);
    }

    @Test
    @DisplayName("만료된 리프레시 토큰으로 요청 시 BadCredentialsException을 던진다")
    void refreshToken_WithExpiredToken_ThrowsBadCredentialsException() {
        // given
        String expiredRefreshToken = "expiredRefreshToken";
        String email = "test@example.com";

        RefreshToken storedToken = RefreshToken.builder()
                .username(email)
                .token(expiredRefreshToken)
                .expiryDate(new Date(System.currentTimeMillis() - 3600000)) // expired
                .build();

        given(jwtTokenProvider.getUsernameFromJWT(expiredRefreshToken)).willReturn(email);
        given(refreshTokenRepository.findById(email)).willReturn(Optional.of(storedToken));

        // when & then
        assertThatThrownBy(() -> authService.refreshToken(expiredRefreshToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("리프레시 토큰 유효시간 만료");
        verify(jwtTokenProvider).validateToken(expiredRefreshToken);
    }

    @Test
    @DisplayName("로그아웃 시 리프레시 토큰을 삭제한다")
    void logout_WithValidRefreshToken_DeletesToken() {
        // given
        String refreshToken = "validRefreshToken";
        String email = "test@example.com";

        given(jwtTokenProvider.getUsernameFromJWT(refreshToken)).willReturn(email);

        // when
        authService.logout(refreshToken);

        // then
        verify(jwtTokenProvider).validateToken(refreshToken);
        verify(jwtTokenProvider).getUsernameFromJWT(refreshToken);
        verify(refreshTokenRepository).deleteById(email);
    }

    @Test
    @DisplayName("로그인 시 리프레시 토큰을 DB에 저장한다")
    void login_SavesRefreshTokenToDatabase() {
        // given
        given(userRepository.findByEmail(loginRequest.getEmail())).willReturn(Optional.of(testUser));
        given(passwordEncoder.matches(loginRequest.getPassword(), testUser.getPassword())).willReturn(true);
        given(jwtTokenProvider.createToken(anyString(), anyString(), any())).willReturn("accessToken");
        given(jwtTokenProvider.createRefreshToken(anyString(), anyString(), any())).willReturn("refreshToken");
        given(jwtTokenProvider.getRefreshExpiration()).willReturn(604800000L);

        // when
        authService.login(loginRequest);

        // then
        ArgumentCaptor<RefreshToken> tokenCaptor = ArgumentCaptor.forClass(RefreshToken.class);
        verify(refreshTokenRepository).save(tokenCaptor.capture());

        RefreshToken savedToken = tokenCaptor.getValue();
        assertThat(savedToken.getUsername()).isEqualTo(testUser.getEmail());
        assertThat(savedToken.getToken()).isEqualTo("refreshToken");
        assertThat(savedToken.getExpiryDate()).isAfter(new Date());
    }
}