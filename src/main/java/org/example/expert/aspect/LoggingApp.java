package org.example.expert.aspect;

import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class LoggingApp {
	/*
	어떤것을 언제 어떻게?
	어드민 사용자만 접근할 수 있는 특정 API에는 접근할 때마다 접근 로그를 기록
	- `org.example.expert.domain.comment.controller.CommentAdminController` 클래스의 `deleteComment()`
	- `org.example.expert.domain.user.controller.UserAdminController` 클래스의 `changeUserRole()`
	를 사용하는 서비스의 하위 메서드들 전후로 AOP가 실행되어야 한다

	로깅 할 내용 : 요청한 사용자의 ID / API 요청 시각 / API 요청 URL / 요청 본문 / 응답 본문
	* */
	@Around("execution(* org.example.expert.domain.comment.controller.CommentAdminController.deleteComment(..)) || " +
		"execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole(..))")
	public Object loggingAdmin(ProceedingJoinPoint joinPoint) throws Throwable {

		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		String requestUrl = request.getRequestURI();
		LocalDateTime now = LocalDateTime.now();
		Long userId = (Long)request.getAttribute("userId");
		Object[] args = joinPoint.getArgs();

		log.info(
			"요청한 사용자의 ID:" + userId + "\n" + "API 요청 시각: " + now + "\n" + "API 요청 URL: " + requestUrl + "\n" + "요청 본문: "
				+ java.util.Arrays.toString(args) + "\n");

		Object response = joinPoint.proceed();
		if (response != null) {
			log.info("요청 본문: " + "\n" + "응답 본문: " + response + "\n");
		}

		return response;
	}
}
