package com.whatthefork.userservice.query.service;

import com.whatthefork.userservice.query.dto.UserDTO;
import com.whatthefork.userservice.query.dto.UserDetailResponse;
import com.whatthefork.userservice.query.dto.UserListResponse;
import com.whatthefork.userservice.query.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserQueryService userQueryService;

    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUserDTO = new UserDTO();
        testUserDTO.setUsername("testuser");
        testUserDTO.setRole("USER");
    }

    @Test
    @DisplayName("유저 ID로 유저 상세 정보를 조회한다")
    void getUserDetail_WithValidUserId_ReturnsUserDetailResponse() {
        // given
        Long userId = 1L;
        given(userMapper.findUserById(userId)).willReturn(testUserDTO);

        // when
        UserDetailResponse result = userQueryService.getUserDetail(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUser()).isEqualTo(testUserDTO);
        assertThat(result.getUser().getUsername()).isEqualTo("testuser");
        verify(userMapper).findUserById(userId);
    }

    @Test
    @DisplayName("존재하지 않는 유저 ID로 조회 시 RuntimeException을 던진다")
    void getUserDetail_WithNonExistentUserId_ThrowsRuntimeException() {
        // given
        Long userId = 999L;
        given(userMapper.findUserById(userId)).willReturn(null);

        // when & then
        assertThatThrownBy(() -> userQueryService.getUserDetail(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("유저 정보 찾지 못함");
        verify(userMapper).findUserById(userId);
    }

    @Test
    @DisplayName("모든 유저 목록을 조회한다")
    void getAllUsers_ReturnsUserListResponse() {
        // given
        UserDTO user1 = new UserDTO();
        user1.setUsername("user1");
        user1.setRole("USER");

        UserDTO user2 = new UserDTO();
        user2.setUsername("user2");
        user2.setRole("ADMIN");

        List<UserDTO> userList = Arrays.asList(user1, user2);
        given(userMapper.findAllUsers()).willReturn(userList);

        // when
        UserListResponse result = userQueryService.getAllUsers();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUsers()).hasSize(2);
        assertThat(result.getUsers().get(0).getUsername()).isEqualTo("user1");
        assertThat(result.getUsers().get(1).getUsername()).isEqualTo("user2");
        verify(userMapper).findAllUsers();
    }

    @Test
    @DisplayName("유저가 없을 때 빈 리스트를 반환한다")
    void getAllUsers_WithNoUsers_ReturnsEmptyList() {
        // given
        given(userMapper.findAllUsers()).willReturn(Arrays.asList());

        // when
        UserListResponse result = userQueryService.getAllUsers();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUsers()).isEmpty();
        verify(userMapper).findAllUsers();
    }

    @Test
    @DisplayName("유저 ID로 유저 등급을 조회한다")
    void getUserGrade_WithValidUserId_ReturnsGrade() {
        // given
        Long userId = 1L;
        given(userMapper.findUserById(userId)).willReturn(testUserDTO);

        // when
        String grade = userQueryService.getUserGrade(userId);

        // then
        assertThat(grade).isEqualTo("PREMIUM");
        verify(userMapper).findUserById(userId);
    }

    @Test
    @DisplayName("존재하지 않는 유저 ID로 등급 조회 시 RuntimeException을 던진다")
    void getUserGrade_WithNonExistentUserId_ThrowsRuntimeException() {
        // given
        Long userId = 999L;
        given(userMapper.findUserById(userId)).willReturn(null);

        // when & then
        assertThatThrownBy(() -> userQueryService.getUserGrade(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("유저 정보 찾지 못함");
        verify(userMapper).findUserById(userId);
    }

    @Test
    @DisplayName("다양한 유저 ID로 등급 조회가 가능하다")
    void getUserGrade_WithDifferentUserIds_ReturnsGrade() {
        // given
        Long[] userIds = {1L, 100L, 9999L};

        for (Long userId : userIds) {
            given(userMapper.findUserById(userId)).willReturn(testUserDTO);

            // when
            String grade = userQueryService.getUserGrade(userId);

            // then
            assertThat(grade).isEqualTo("PREMIUM");
        }
    }
}