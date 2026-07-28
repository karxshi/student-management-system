package ru.synergy.sms.student_managment_system.dto.course;

public record CourseResponse(
        Long id,
        String name,
        String description,
        Integer durationHours
) {
}
