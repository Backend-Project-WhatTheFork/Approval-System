package com.whatthefork.userservice.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private String testSecret = "+i12AQFwws/Del1pGAOB0imXQLGaCNd7dyMF41Mxe6k=";
    private long testExpiration = 3600000L; // 1 hour
    private long testRefreshExpiration = 604800000L; // 7 days
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", testSecret);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpiration", testExpiration);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtRefreshExpiration", testRefreshExpiration);
        jwtTokenProvider.init();

        byte[] keyBytes = Decoders.BASE64.decode(testSecret);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Test
    @DisplayName("액세스 토큰을 생성한다")
    void createToken_CreatesValidAccessToken() {
        // given
        String username = "test@example.com";
        String role = "USER";
        Long userId = 1L;

        // when
        String token = jwtTokenProvider.createToken(username, role, userId);

        // then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    @DisplayName("리프레시 토큰을 생성한다")
    void createRefreshToken_CreatesValidRefreshToken() {
        // given
        String username = "test@example.com";
        String role = "USER";
        Long userId = 1L;

        // when
        String refreshToken = jwtTokenProvider.createRefreshToken(username, role, userId);

        // then
        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken).isNotEmpty();
    }

    @Test
    @DisplayName("리프레시 토큰의 만료 시간을 반환한다")
    void getRefreshExpiration_ReturnsCorrectValue() {
        // when
        long expiration = jwtTokenProvider.getRefreshExpiration();

        // then
        assertThat(expiration).isEqualTo(testRefreshExpiration);
    }

    @Test
    @DisplayName("유효한 토큰을 검증하면 true를 반환한다")
    void validateToken_WithValidToken_ReturnsTrue() {
        // given
        String token = jwtTokenProvider.createToken("test@example.com", "USER", 1L);

        // when
        boolean result = jwtTokenProvider.validateToken(token);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("만료된 토큰을 검증하면 BadCredentialsException을 던진다")
    void validateToken_WithExpiredToken_ThrowsBadCredentialsException() {
        // given
        String expiredToken = createExpiredToken("test@example.com", "USER", 1L);

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(expiredToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Expired JWT Token");
    }

    @Test
    @DisplayName("잘못된 형식의 토큰을 검증하면 BadCredentialsException을 던진다")
    void validateToken_WithMalformedToken_ThrowsBadCredentialsException() {
        // given
        String malformedToken = "invalid.token.here";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(malformedToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Invalid JWT Token");
    }

    @Test
    @DisplayName("JWT 토큰에서 username을 추출한다")
    void getUsernameFromJWT_WithValidToken_ReturnsUsername() {
        // given
        String expectedUsername = "test@example.com";
        String token = jwtTokenProvider.createToken(expectedUsername, "USER", 1L);

        // when
        String actualUsername = jwtTokenProvider.getUsernameFromJWT(token);

        // then
        assertThat(actualUsername).isEqualTo(expectedUsername);
    }

    @Test
    @DisplayName("다양한 username으로 JWT 토큰을 생성하고 추출한다")
    void createAndExtractUsername_WithDifferentUsernames_WorksCorrectly() {
        // given
        String[] usernames = {"user1@example.com", "admin@example.com", "test@test.com"};

        for (String expectedUsername : usernames) {
            // when
            String token = jwtTokenProvider.createToken(expectedUsername, "USER", 1L);
            String actualUsername = jwtTokenProvider.getUsernameFromJWT(token);

            // then
            assertThat(actualUsername).isEqualTo(expectedUsername);
        }
    }

    @Test
    @DisplayName("액세스 토큰과 리프레시 토큰이 다르게 생성된다")
    void createToken_AndRefreshToken_AreDifferent() {
        // given
        String username = "test@example.com";
        String role = "USER";
        Long userId = 1L;

        // when
        String accessToken = jwtTokenProvider.createToken(username, role, userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(username, role, userId);

        // then
        assertThat(accessToken).isNotEqualTo(refreshToken);
    }

    @Test
    @DisplayName("다양한 role과 userId로 토큰을 생성한다")
    void createToken_WithDifferentRolesAndUserIds_CreatesValidTokens() {
        // given
        String[][] testCases = {
                {"USER", "1"},
                {"ADMIN", "100"},
                {"MANAGER", "9999"}
        };

        for (String[] testCase : testCases) {
            String role = testCase[0];
            Long userId = Long.parseLong(testCase[1]);

            // when
            String token = jwtTokenProvider.createToken("test@example.com", role, userId);

            // then
            assertThat(token).isNotNull();
            boolean isValid = jwtTokenProvider.validateToken(token);
            assertThat(isValid).isTrue();
        }
    }

    @Test
    @DisplayName("토큰에 올바른 클레임이 포함되어 있다")
    void createToken_ContainsCorrectClaims() {
        // given
        String username = "test@example.com";
        String role = "USER";
        Long userId = 123L;

        // when
        String token = jwtTokenProvider.createToken(username, role, userId);

        // then
        var claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertThat(claims.getSubject()).isEqualTo(username);
        assertThat(claims.get("role", String.class)).isEqualTo(role);
        assertThat(claims.get("userId", Long.class)).isEqualTo(userId);
    }

    @Test
    @DisplayName("빈 문자열 토큰을 검증하면 BadCredentialsException을 던진다")
    void validateToken_WithEmptyToken_ThrowsBadCredentialsException() {
        // given
        String emptyToken = "";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(emptyToken))
                .isInstanceOf(BadCredentialsException.class);
    }

    // Helper method
    private String createExpiredToken(String username, String role, Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() - 3600000); // expired 1 hour ago

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .claim("userId", userId)
                .issuedAt(new Date(now.getTime() - 7200000))
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }
}