package ru.synergy.sms.student_managment_system;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.synergy.sms.student_managment_system.dto.student.CreateStudentRequest;
import ru.synergy.sms.student_managment_system.dto.student.StudentResponse;
import ru.synergy.sms.student_managment_system.dto.student.UpdateStudentRequest;
import ru.synergy.sms.student_managment_system.entity.student.Student;
import ru.synergy.sms.student_managment_system.exception.student.EmailAlreadyExistsException;
import ru.synergy.sms.student_managment_system.exception.student.StudentNotFoundException;
import ru.synergy.sms.student_managment_system.mapper.student.StudentMapper;
import ru.synergy.sms.student_managment_system.repository.student.StudentRepository;
import ru.synergy.sms.student_managment_system.service.student.StudentServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentResponse studentResponse;

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

        studentResponse = new StudentResponse(
                1L,
                "Иван",
                "Иванов",
                "ivanov@example.com",
                "ИВТ-101",
                1
        );
    }

    @Test
    void shouldCreateStudent() {
        CreateStudentRequest request = new CreateStudentRequest(
                "Иван",
                "Иванов",
                "ivanov@example.com",
                "ИВТ-101",
                1
        );

        when(studentRepository.existsByEmail(request.email()))
                .thenReturn(false);
        when(studentMapper.toEntity(request))
                .thenReturn(student);
        when(studentRepository.save(student))
                .thenReturn(student);
        when(studentMapper.toResponse(student))
                .thenReturn(studentResponse);

        StudentResponse result = studentService.create(request);

        assertEquals(studentResponse, result);

        verify(studentRepository).existsByEmail(request.email());
        verify(studentRepository).save(student);
        verify(studentMapper).toResponse(student);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        CreateStudentRequest request = new CreateStudentRequest(
                "Иван",
                "Иванов",
                "ivanov@example.com",
                "ИВТ-101",
                1
        );

        when(studentRepository.existsByEmail(request.email()))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> studentService.create(request)
        );

        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    void shouldReturnStudentById() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(studentMapper.toResponse(student))
                .thenReturn(studentResponse);

        StudentResponse result = studentService.getById(1L);

        assertEquals(studentResponse, result);

        verify(studentRepository).findById(1L);
        verify(studentMapper).toResponse(student);
    }

    @Test
    void shouldThrowExceptionWhenStudentNotFound() {
        when(studentRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.getById(999L)
        );
    }

    @Test
    void shouldUpdateStudent() {
        UpdateStudentRequest request = new UpdateStudentRequest(
                "Петр",
                "Петров",
                "petrov@example.com",
                "ИВТ-201",
                2
        );

        StudentResponse updatedResponse = new StudentResponse(
                1L,
                "Петр",
                "Петров",
                "petrov@example.com",
                "ИВТ-201",
                2
        );

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(studentRepository.existsByEmailAndIdNot(request.email(), 1L))
                .thenReturn(false);
        when(studentRepository.save(student))
                .thenReturn(student);
        when(studentMapper.toResponse(student))
                .thenReturn(updatedResponse);

        StudentResponse result = studentService.update(1L, request);

        assertEquals(updatedResponse, result);
        assertEquals("Петр", student.getFirstName());
        assertEquals("Петров", student.getLastName());
        assertEquals("petrov@example.com", student.getEmail());
        assertEquals("ИВТ-201", student.getGroupName());
        assertEquals(2, student.getCourseNumber());

        verify(studentRepository).findById(1L);
        verify(studentRepository).save(student);
    }

    @Test
    void shouldDeleteStudent() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        studentService.delete(1L);

        verify(studentRepository).delete(student);
    }
}
