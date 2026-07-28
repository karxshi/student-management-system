package ru.synergy.sms.student_managment_system.dto.course;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateCourseRequest(

        @NotBlank(message = "Название курса не должно быть пустым")
        @Size(max = 150, message = "Название курса не должно превышать 150 символов")
        String name,

        @Size(max = 1000, message = "Описание курса не должно превышать 1000 символов")
        String description,

        @NotNull(message = "Продолжительность курса должна быть указана")
        @Min(value = 1, message = "Продолжительность курса должна быть не меньше 1 часа")
        @Max(value = 10000, message = "Продолжительность курса не должна превышать 10000 часов")
        Integer durationHours
) {
}
