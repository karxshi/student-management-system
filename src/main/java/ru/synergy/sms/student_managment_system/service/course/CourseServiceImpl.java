package ru.synergy.sms.student_managment_system.service.course;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.synergy.sms.student_managment_system.dto.course.CourseResponse;
import ru.synergy.sms.student_managment_system.dto.course.CreateCourseRequest;
import ru.synergy.sms.student_managment_system.dto.course.UpdateCourseRequest;
import ru.synergy.sms.student_managment_system.entity.course.Course;
import ru.synergy.sms.student_managment_system.exception.course.CourseNameAlreadyExistsException;
import ru.synergy.sms.student_managment_system.exception.course.CourseNotFoundException;
import ru.synergy.sms.student_managment_system.mapper.course.CourseMapper;
import ru.synergy.sms.student_managment_system.repository.course.CourseRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    public CourseResponse create(CreateCourseRequest request) {
        if (courseRepository.existsByNameIgnoreCase(request.name())) {
            throw new CourseNameAlreadyExistsException(request.name());
        }

        Course course = courseMapper.toEntity(request);
        Course savedCourse = courseRepository.save(course);

        return courseMapper.toResponse(savedCourse);
    }

    @Override
    public CourseResponse getById(Long id) {
        Course course = findCourseById(id);
        return courseMapper.toResponse(course);
    }

    @Override
    public List<CourseResponse> getAll() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CourseResponse update(Long id, UpdateCourseRequest request) {
        Course course = findCourseById(id);

        if (courseRepository.existsByNameIgnoreCaseAndIdNot(request.name(), id)) {
            throw new CourseNameAlreadyExistsException(request.name());
        }

        course.setName(request.name());
        course.setDescription(request.description());
        course.setDurationHours(request.durationHours());

        Course updatedCourse = courseRepository.save(course);

        return courseMapper.toResponse(updatedCourse);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Course course = findCourseById(id);
        courseRepository.delete(course);
    }

    private Course findCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException(id));
    }
}
