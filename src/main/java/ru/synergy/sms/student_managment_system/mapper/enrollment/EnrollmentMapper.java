package ru.synergy.sms.student_managment_system.mapper.enrollment;

import org.springframework.stereotype.Component;
import ru.synergy.sms.student_managment_system.dto.enrollment.EnrollmentResponse;
import ru.synergy.sms.student_managment_system.entity.course.Course;
import ru.synergy.sms.student_managment_system.entity.enrollment.Enrollment;
import ru.synergy.sms.student_managment_system.entity.student.Student;

@Component
public class EnrollmentMapper {

    public EnrollmentResponse toResponse(Enrollment enrollment) {
        Student student = enrollment.getStudent();
        Course course = enrollment.getCourse();

        return new EnrollmentResponse(
                enrollment.getId(),
                student.getId(),
                student.getFirstName() + " " + student.getLastName(),
                course.getId(),
                course.getName(),
                enrollment.getEnrollmentDate(),
                enrollment.getStatus(),
                enrollment.getGrade()
        );
    }
}
