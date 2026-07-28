package ru.synergy.sms.student_managment_system.dto.enrollment;

import ru.synergy.sms.student_managment_system.entity.enrollment.EnrollmentStatus;
import ru.synergy.sms.student_managment_system.entity.enrollment.Grade;

import java.time.LocalDate;

public record EnrollmentResponse(
        Long id,
        Long studentId,
        String studentName,
        Long courseId,
        String courseName,
        LocalDate enrollmentDate,
        EnrollmentStatus status,
        Grade grade
) {
}
