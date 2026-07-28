package ru.synergy.sms.student_managment_system.service.enrollment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.synergy.sms.student_managment_system.dto.enrollment.CreateEnrollmentRequest;
import ru.synergy.sms.student_managment_system.dto.enrollment.EnrollmentResponse;
import ru.synergy.sms.student_managment_system.dto.enrollment.UpdateGradeRequest;
import ru.synergy.sms.student_managment_system.entity.course.Course;
import ru.synergy.sms.student_managment_system.entity.enrollment.Enrollment;
import ru.synergy.sms.student_managment_system.entity.enrollment.EnrollmentStatus;
import ru.synergy.sms.student_managment_system.entity.enrollment.Grade;
import ru.synergy.sms.student_managment_system.entity.student.Student;
import ru.synergy.sms.student_managment_system.exception.course.CourseNotFoundException;
import ru.synergy.sms.student_managment_system.exception.enrollment.EnrollmentAlreadyExistsException;
import ru.synergy.sms.student_managment_system.exception.enrollment.EnrollmentNotFoundException;
import ru.synergy.sms.student_managment_system.exception.student.StudentNotFoundException;
import ru.synergy.sms.student_managment_system.mapper.enrollment.EnrollmentMapper;
import ru.synergy.sms.student_managment_system.repository.course.CourseRepository;
import ru.synergy.sms.student_managment_system.repository.enrollment.EnrollmentRepository;
import ru.synergy.sms.student_managment_system.repository.student.StudentRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    @Transactional
    public EnrollmentResponse create(CreateEnrollmentRequest request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() ->
                        new StudentNotFoundException(request.studentId()));

        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() ->
                        new CourseNotFoundException(request.courseId()));

        if (enrollmentRepository.existsByStudentIdAndCourseId(
                request.studentId(),
                request.courseId()
        )) {
            throw new EnrollmentAlreadyExistsException(
                    request.studentId(),
                    request.courseId()
            );
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .enrollmentDate(LocalDate.now())
                .status(EnrollmentStatus.ACTIVE)
                .build();

        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    public List<EnrollmentResponse> getByStudentId(Long studentId) {
        return enrollmentRepository.findAllByStudentId(studentId)
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<EnrollmentResponse> getByCourseId(Long courseId) {
        return enrollmentRepository.findAllByCourseId(courseId)
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));

        enrollmentRepository.delete(enrollment);
    }

    @Override
    @Transactional
    public EnrollmentResponse updateGrade(
            Long enrollmentId,
            UpdateGradeRequest request
    ) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() ->
                        new EnrollmentNotFoundException(enrollmentId));

        enrollment.setGrade(request.grade());

        if (request.grade().getValue() >= Grade.SATISFACTORY.getValue()) {
            enrollment.setStatus(EnrollmentStatus.COMPLETED);
        }

        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }
}
