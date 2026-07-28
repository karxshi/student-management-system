package ru.synergy.sms.student_managment_system.exception.enrollment;

public class EnrollmentAlreadyExistsException extends RuntimeException {

    public EnrollmentAlreadyExistsException(Long studentId, Long courseId) {
        super(String.format("Студент %d уже записан на курс %d", studentId, courseId));
    }
}
