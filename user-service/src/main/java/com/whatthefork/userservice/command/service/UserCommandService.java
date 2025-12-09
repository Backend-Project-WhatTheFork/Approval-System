package com.whatthefork.userservice.command.service;

import com.whatthefork.userservice.command.dto.UserCreateRequest;
import com.whatthefork.userservice.command.entity.User;
import com.whatthefork.userservice.command.entity.UserRole;
import com.whatthefork.userservice.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.whatthefork.userservice.exception.DuplicateEmailException;


@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(UserCreateRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다: " + request.getEmail());
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .positionCode(request.getPositionCode())
                .deptId(request.getDeptId())
                .isDeptLeader(request.isDeptLeader())
                .isAdmin(request.isAdmin())
                .role(request.isAdmin() ? UserRole.ADMIN : UserRole.USER)
                .build();
        userRepository.save(user);
    }
}

