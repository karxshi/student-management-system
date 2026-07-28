package ru.synergy.sms.student_managment_system.exception.student;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super(String.format("Студент с email %s уже существует", email));
    }
}
