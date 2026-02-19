package com.codes.study_core_service.session.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


public record SessionResponse(
        Long id,
        Long categoryId,
        String status,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long totalSeconds,
        String note
) {
}
