package ru.synergy.sms.student_managment_system.exception.course;

public class CourseNotFoundException extends RuntimeException {

    public CourseNotFoundException(Long id) {
        super(String.format("Курс с идентификатором %s не найден", id));
    }
}
