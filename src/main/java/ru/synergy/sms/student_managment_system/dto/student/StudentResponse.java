package ru.synergy.sms.student_managment_system.dto.student;

public record StudentResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String groupName,
        Integer courseNumber
) {
}
