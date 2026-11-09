package org.example.expert.domain.user.repository;

import java.util.List;

import org.example.expert.domain.user.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
	List<PasswordHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
}
