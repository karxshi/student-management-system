package ru.synergy.sms.student_managment_system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.synergy.sms.student_managment_system.dto.enrollment.CreateEnrollmentRequest;
import ru.synergy.sms.student_managment_system.dto.enrollment.EnrollmentResponse;
import ru.synergy.sms.student_managment_system.entity.course.Course;
import ru.synergy.sms.student_managment_system.entity.enrollment.Enrollment;
import ru.synergy.sms.student_managment_system.entity.enrollment.EnrollmentStatus;
import ru.synergy.sms.student_managment_system.entity.student.Student;
import ru.synergy.sms.student_managment_system.exception.course.CourseNotFoundException;
import ru.synergy.sms.student_managment_system.exception.enrollment.EnrollmentAlreadyExistsException;
import ru.synergy.sms.student_managment_system.exception.enrollment.EnrollmentNotFoundException;
import ru.synergy.sms.student_managment_system.exception.student.StudentNotFoundException;
import ru.synergy.sms.student_managment_system.mapper.enrollment.EnrollmentMapper;
import ru.synergy.sms.student_managment_system.repository.course.CourseRepository;
import ru.synergy.sms.student_managment_system.repository.enrollment.EnrollmentRepository;
import ru.synergy.sms.student_managment_system.repository.student.StudentRepository;
import ru.synergy.sms.student_managment_system.service.enrollment.EnrollmentServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentMapper enrollmentMapper;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private Student student;
    private Course course;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .firstName("Иван")
                .lastName("Иванов")
                .email("ivanov@example.com")
                .groupName("ИВТ-101")
                .courseNumber(1)
                .build();

        course = Course.builder()
                .id(10L)
                .name("Основы Java")
                .description("Базовый курс по Java")
                .durationHours(72)
                .build();

        enrollment = Enrollment.builder()
                .id(100L)
                .student(student)
                .course(course)
                .enrollmentDate(LocalDate.of(2026, 7, 28))
                .status(EnrollmentStatus.ACTIVE)
                .grade(null)
                .build();
    }

    @Test
    void shouldCreateEnrollment() {
        CreateEnrollmentRequest request =
                new CreateEnrollmentRequest(1L, 10L);

        EnrollmentResponse response = new EnrollmentResponse(
                100L,
                1L,
                "Иван Иванов",
                10L,
                "Основы Java",
                LocalDate.now(),
                EnrollmentStatus.ACTIVE,
                null
        );

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(courseRepository.findById(10L))
                .thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 10L))
                .thenReturn(false);

        when(enrollmentRepository.save(any(Enrollment.class)))
                .thenAnswer(invocation -> {
                    Enrollment saved = invocation.getArgument(0);
                    saved.setId(100L);
                    return saved;
                });

        when(enrollmentMapper.toResponse(any(Enrollment.class)))
                .thenReturn(response);

        EnrollmentResponse result = enrollmentService.create(request);

        assertEquals(100L, result.id());
        assertEquals(1L, result.studentId());
        assertEquals("Иван Иванов", result.studentName());
        assertEquals(10L, result.courseId());
        assertEquals("Основы Java", result.courseName());
        assertEquals(LocalDate.now(), result.enrollmentDate());
        assertEquals(EnrollmentStatus.ACTIVE, result.status());
        assertNull(result.grade());

        verify(studentRepository).findById(1L);
        verify(courseRepository).findById(10L);
        verify(enrollmentRepository)
                .existsByStudentIdAndCourseId(1L, 10L);
        verify(enrollmentRepository).save(any(Enrollment.class));
        verify(enrollmentMapper).toResponse(any(Enrollment.class));
    }

    @Test
    void shouldThrowExceptionWhenStudentNotFound() {
        CreateEnrollmentRequest request =
                new CreateEnrollmentRequest(999L, 10L);

        when(studentRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> enrollmentService.create(request)
        );

        verify(courseRepository, never()).findById(10L);
        verify(enrollmentRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenCourseNotFound() {
        CreateEnrollmentRequest request =
                new CreateEnrollmentRequest(1L, 999L);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(courseRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                CourseNotFoundException.class,
                () -> enrollmentService.create(request)
        );

        verify(enrollmentRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenEnrollmentAlreadyExists() {
        CreateEnrollmentRequest request =
                new CreateEnrollmentRequest(1L, 10L);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(courseRepository.findById(10L))
                .thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 10L))
                .thenReturn(true);

        assertThrows(
                EnrollmentAlreadyExistsException.class,
                () -> enrollmentService.create(request)
        );

        verify(enrollmentRepository, never())
                .save(any());
    }

    @Test
    void shouldReturnEnrollmentsByStudentId() {
        EnrollmentResponse response = new EnrollmentResponse(
                100L,
                1L,
                "Иван Иванов",
                10L,
                "Основы Java",
                enrollment.getEnrollmentDate(),
                EnrollmentStatus.ACTIVE,
                null
        );

        when(enrollmentRepository.findAllByStudentId(1L))
                .thenReturn(List.of(enrollment));

        when(enrollmentMapper.toResponse(enrollment))
                .thenReturn(response);

        List<EnrollmentResponse> result =
                enrollmentService.getByStudentId(1L);

        assertEquals(1, result.size());
        assertEquals(100L, result.getFirst().id());
        assertEquals(1L, result.getFirst().studentId());
        assertEquals(10L, result.getFirst().courseId());
        assertEquals("Основы Java", result.getFirst().courseName());

        verify(enrollmentRepository).findAllByStudentId(1L);
        verify(enrollmentMapper).toResponse(enrollment);
    }

    @Test
    void shouldReturnEnrollmentsByCourseId() {
        EnrollmentResponse response = new EnrollmentResponse(
                100L,
                1L,
                "Иван Иванов",
                10L,
                "Основы Java",
                enrollment.getEnrollmentDate(),
                EnrollmentStatus.ACTIVE,
                null
        );

        when(enrollmentRepository.findAllByCourseId(10L))
                .thenReturn(List.of(enrollment));

        when(enrollmentMapper.toResponse(enrollment))
                .thenReturn(response);

        List<EnrollmentResponse> result =
                enrollmentService.getByCourseId(10L);

        assertEquals(1, result.size());
        assertEquals(100L, result.getFirst().id());
        assertEquals(1L, result.getFirst().studentId());
        assertEquals("Иван Иванов", result.getFirst().studentName());
        assertEquals(10L, result.getFirst().courseId());

        verify(enrollmentRepository).findAllByCourseId(10L);
        verify(enrollmentMapper).toResponse(enrollment);
    }

    @Test
    void shouldDeleteEnrollment() {
        when(enrollmentRepository.findById(100L))
                .thenReturn(Optional.of(enrollment));

        enrollmentService.delete(100L);

        verify(enrollmentRepository).findById(100L);
        verify(enrollmentRepository).delete(enrollment);
    }

    @Test
    void shouldThrowExceptionWhenEnrollmentNotFoundDuringDelete() {
        when(enrollmentRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                EnrollmentNotFoundException.class,
                () -> enrollmentService.delete(999L)
        );

        verify(enrollmentRepository, never())
                .delete(any());
    }
}
