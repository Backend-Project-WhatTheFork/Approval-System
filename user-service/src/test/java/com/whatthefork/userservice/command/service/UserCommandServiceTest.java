package com.whatthefork.userservice.command.service;

import com.whatthefork.userservice.command.dto.UserCreateRequest;
import com.whatthefork.userservice.command.entity.User;
import com.whatthefork.userservice.command.entity.UserRole;
import com.whatthefork.userservice.command.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserCommandService userCommandService;

    private UserCreateRequest userCreateRequest;

    @BeforeEach
    void setUp() {
        userCreateRequest = new UserCreateRequest();
        userCreateRequest.setName("Test User");
        userCreateRequest.setEmail("test@example.com");
        userCreateRequest.setPassword("password123");
        userCreateRequest.setPositionCode(1);
        userCreateRequest.setDeptId("DEPT001");
        userCreateRequest.setDeptLeader(false);
        userCreateRequest.setAdmin(false);
    }

    @Test
    @DisplayName("일반 유저를 성공적으로 등록한다")
    void registerUser_WithRegularUser_SavesUser() {
        // given
        String encodedPassword = "encodedPassword123";
        given(passwordEncoder.encode(userCreateRequest.getPassword())).willReturn(encodedPassword);

        // when
        userCommandService.registerUser(userCreateRequest);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getName()).isEqualTo("Test User");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
        assertThat(savedUser.getPositionCode()).isEqualTo(1);
        assertThat(savedUser.getDeptId()).isEqualTo("DEPT001");
        assertThat(savedUser.isDeptLeader()).isFalse();
        assertThat(savedUser.isAdmin()).isFalse();
        assertThat(savedUser.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("관리자 유저를 성공적으로 등록한다")
    void registerUser_WithAdminUser_SavesAdminUser() {
        // given
        userCreateRequest.setAdmin(true);
        String encodedPassword = "encodedPassword123";
        given(passwordEncoder.encode(userCreateRequest.getPassword())).willReturn(encodedPassword);

        // when
        userCommandService.registerUser(userCreateRequest);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.isAdmin()).isTrue();
        assertThat(savedUser.getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("부서장 유저를 성공적으로 등록한다")
    void registerUser_WithDeptLeader_SavesDeptLeader() {
        // given
        userCreateRequest.setDeptLeader(true);
        String encodedPassword = "encodedPassword123";
        given(passwordEncoder.encode(userCreateRequest.getPassword())).willReturn(encodedPassword);

        // when
        userCommandService.registerUser(userCreateRequest);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.isDeptLeader()).isTrue();
    }

    @Test
    @DisplayName("비밀번호를 암호화하여 저장한다")
    void registerUser_EncodesPassword() {
        // given
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";
        userCreateRequest.setPassword(rawPassword);
        given(passwordEncoder.encode(rawPassword)).willReturn(encodedPassword);

        // when
        userCommandService.registerUser(userCreateRequest);

        // then
        verify(passwordEncoder).encode(rawPassword);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getPassword()).isEqualTo(encodedPassword);
        assertThat(savedUser.getPassword()).isNotEqualTo(rawPassword);
    }

    @Test
    @DisplayName("관리자이면서 부서장인 유저를 등록한다")
    void registerUser_WithAdminAndDeptLeader_SavesBoth() {
        // given
        userCreateRequest.setAdmin(true);
        userCreateRequest.setDeptLeader(true);
        String encodedPassword = "encodedPassword123";
        given(passwordEncoder.encode(anyString())).willReturn(encodedPassword);

        // when
        userCommandService.registerUser(userCreateRequest);

        // then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.isAdmin()).isTrue();
        assertThat(savedUser.isDeptLeader()).isTrue();
        assertThat(savedUser.getRole()).isEqualTo(UserRole.ADMIN);
    }

    @Test
    @DisplayName("다양한 포지션 코드로 유저를 등록한다")
    void registerUser_WithDifferentPositionCodes_SavesCorrectly() {
        // given
        int[] positionCodes = {1, 2, 3, 4, 5};
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");

        for (int positionCode : positionCodes) {
            userCreateRequest.setPositionCode(positionCode);

            // when
            userCommandService.registerUser(userCreateRequest);

            // then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getPositionCode()).isEqualTo(positionCode);
        }
    }

    @Test
    @DisplayName("다양한 부서 ID로 유저를 등록한다")
    void registerUser_WithDifferentDeptIds_SavesCorrectly() {
        // given
        String[] deptIds = {"DEPT001", "DEPT002", "DEPT003"};
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");

        for (String deptId : deptIds) {
            userCreateRequest.setDeptId(deptId);

            // when
            userCommandService.registerUser(userCreateRequest);

            // then
            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());

            User savedUser = userCaptor.getValue();
            assertThat(savedUser.getDeptId()).isEqualTo(deptId);
        }
    }
}