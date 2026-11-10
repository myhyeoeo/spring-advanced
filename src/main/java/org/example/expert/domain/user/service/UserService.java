package org.example.expert.domain.user.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.PasswordHistory;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.PasswordHistoryRepository;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordHistoryRepository passwordHistoryRepository;

    @Transactional(readOnly = true)
    public UserResponse getUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));
        return new UserResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public void changePassword(long userId, UserChangePasswordRequest userChangePasswordRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }

        List<PasswordHistory> histories = passwordHistoryRepository.findTop3ByUserIdOrderByCreatedAtDesc(userId);
        List<PasswordHistory> recentHistories = histories.stream().limit(3).toList();
        for (PasswordHistory history : recentHistories) {
            if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), history.getPassword())) {
                throw new InvalidRequestException("최근에 사용한 비밀번호는 사용할 수 없습니다.");
            }
        }

        passwordHistoryRepository.save(new PasswordHistory(userId, user.getPassword()));

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }
}
