package ru.synergy.sms.student_managment_system.entity.enrollment;

import lombok.Getter;

@Getter
public enum Grade {
    TERRIBLE(1),
    FAIL(2),
    SATISFACTORY(3),
    GOOD(4),
    EXCELLENT(5);

    private final int value;

    Grade(int value) {
        this.value = value;
    }

}
