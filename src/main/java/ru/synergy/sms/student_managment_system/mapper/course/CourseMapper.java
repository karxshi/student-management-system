package ru.synergy.sms.student_managment_system.mapper.course;

import org.springframework.stereotype.Component;
import ru.synergy.sms.student_managment_system.dto.course.CourseResponse;
import ru.synergy.sms.student_managment_system.dto.course.CreateCourseRequest;
import ru.synergy.sms.student_managment_system.entity.course.Course;

@Component
public class CourseMapper {

    public Course toEntity(CreateCourseRequest request) {
        return Course.builder()
                .name(request.name())
                .description(request.description())
                .durationHours(request.durationHours())
                .build();
    }

    public CourseResponse toResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getName(),
                course.getDescription(),
                course.getDurationHours()
        );
    }
}
