package ru.synergy.sms.student_managment_system.exception.enrollment;

public class EnrollmentNotFoundException extends RuntimeException {

    public EnrollmentNotFoundException(Long id) {
        super(String.format("Запись на курс с идентификатором %d не найдена", id));
    }
}
