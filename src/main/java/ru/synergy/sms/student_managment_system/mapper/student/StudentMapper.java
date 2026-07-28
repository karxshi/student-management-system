package ru.synergy.sms.student_managment_system.mapper.student;

import org.springframework.stereotype.Component;
import ru.synergy.sms.student_managment_system.dto.student.CreateStudentRequest;
import ru.synergy.sms.student_managment_system.dto.student.StudentResponse;
import ru.synergy.sms.student_managment_system.entity.course.Course;
import ru.synergy.sms.student_managment_system.entity.student.Student;

@Component
public class StudentMapper {

    public Student toEntity(
            CreateStudentRequest request,
            Course course
    ) {
        return Student.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .groupName(request.groupName())
                .courseNumber(request.courseNumber())
                .course(course)
                .build();
    }

    public StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getGroupName(),
                student.getCourseNumber(),
                student.getCourse().getId(),
                student.getCourse().getName()
        );
    }
}
