package ru.synergy.sms.student_managment_system.dto.enrollment;

import jakarta.validation.constraints.NotNull;
import ru.synergy.sms.student_managment_system.entity.enrollment.Grade;

public record UpdateGradeRequest(

        @NotNull(message = "Оценка должна быть указана")
        Grade grade
) {
}
