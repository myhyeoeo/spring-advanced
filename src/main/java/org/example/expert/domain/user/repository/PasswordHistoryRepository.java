package org.example.expert.domain.user.repository;

import java.util.List;

import org.example.expert.domain.user.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
	// @Query("SELECT p FROM PasswordHistory p WHERE p.userId = :userId ORDER BY p.createdAt DESC")
	// List<PasswordHistory> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

	List<PasswordHistory> findTop3ByUserIdOrderByCreatedAtDesc(Long userId);

	//삭제 대비용
	void deleteAllByUserId(Long userId);
}
