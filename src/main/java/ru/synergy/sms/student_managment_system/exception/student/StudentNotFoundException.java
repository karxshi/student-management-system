package ru.synergy.sms.student_managment_system.exception.student;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Long id) {
        super(String.format("Студент с идентификатором %s не найден", id));
    }
}
