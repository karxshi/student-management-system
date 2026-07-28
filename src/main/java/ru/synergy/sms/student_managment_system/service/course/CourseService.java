package ru.synergy.sms.student_managment_system.service.course;

import ru.synergy.sms.student_managment_system.dto.course.CourseResponse;
import ru.synergy.sms.student_managment_system.dto.course.CreateCourseRequest;
import ru.synergy.sms.student_managment_system.dto.course.UpdateCourseRequest;

import java.util.List;

public interface CourseService {

    CourseResponse create(CreateCourseRequest request);

    CourseResponse getById(Long id);

    List<CourseResponse> getAll();

    CourseResponse update(Long id, UpdateCourseRequest request);

    void delete(Long id);
}
