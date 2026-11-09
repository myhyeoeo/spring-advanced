package org.example.expert.domain.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "password_history")
public class PasswordHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId; // 또는 User 엔티티와 연관관계 매핑

	private String password; // 암호화된 비밀번호

	private LocalDateTime createdAt;

	public PasswordHistory(Long userId, String password) {
		this.userId = userId;
		this.password = password;
		this.createdAt = LocalDateTime.now();
	}
}