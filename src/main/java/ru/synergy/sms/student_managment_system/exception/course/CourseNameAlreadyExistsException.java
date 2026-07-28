package ru.synergy.sms.student_managment_system.exception.course;

public class CourseNameAlreadyExistsException extends RuntimeException {

    public CourseNameAlreadyExistsException(String name) {
        super(String.format("Курс с названием \"%s\" уже существует", name));
    }
}