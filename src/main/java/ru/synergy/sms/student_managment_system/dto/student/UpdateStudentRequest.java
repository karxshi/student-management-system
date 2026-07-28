package ru.synergy.sms.student_managment_system.dto.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateStudentRequest(

        @NotBlank(message = "Имя не должно быть пустым")
        @Size(max = 100)
        String firstName,

        @NotBlank(message = "Фамилия не должна быть пустой")
        @Size(max = 100)
        String lastName,

        @NotBlank(message = "Email не должен быть пустым")
        @Email(message = "Некорректный формат email")
        String email,

        @NotBlank(message = "Название группы не должно быть пустым")
        @Size(max = 50)
        String groupName,

        @NotNull(message = "Номер курса должен быть указан")
        @Min(value = 1)
        @Max(value = 6)
        Integer courseNumber
) {
}