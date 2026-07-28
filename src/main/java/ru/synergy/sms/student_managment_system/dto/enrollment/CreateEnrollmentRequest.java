package ru.synergy.sms.student_managment_system.dto.enrollment;

import jakarta.validation.constraints.NotNull;

public record CreateEnrollmentRequest(
        @NotNull Long studentId,
        @NotNull Long courseId
) {
}
