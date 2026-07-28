package ru.synergy.sms.student_managment_system;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.synergy.sms.student_managment_system.controller.enrollment.EnrollmentController;
import ru.synergy.sms.student_managment_system.dto.enrollment.CreateEnrollmentRequest;
import ru.synergy.sms.student_managment_system.dto.enrollment.EnrollmentResponse;
import ru.synergy.sms.student_managment_system.entity.enrollment.EnrollmentStatus;
import ru.synergy.sms.student_managment_system.exception.GlobalExceptionHandler;
import ru.synergy.sms.student_managment_system.exception.course.CourseNotFoundException;
import ru.synergy.sms.student_managment_system.exception.enrollment.EnrollmentAlreadyExistsException;
import ru.synergy.sms.student_managment_system.exception.enrollment.EnrollmentNotFoundException;
import ru.synergy.sms.student_managment_system.exception.student.StudentNotFoundException;
import ru.synergy.sms.student_managment_system.service.enrollment.EnrollmentService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnrollmentController.class)
@Import(GlobalExceptionHandler.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private EnrollmentService enrollmentService;

    @Test
    void shouldCreateEnrollment() throws Exception {
        CreateEnrollmentRequest request =
                new CreateEnrollmentRequest(1L, 10L);

        EnrollmentResponse response = new EnrollmentResponse(
                100L,
                1L,
                "Иван Иванов",
                10L,
                "Основы Java",
                LocalDate.of(2026, 7, 28),
                EnrollmentStatus.ACTIVE,
                null
        );

        when(enrollmentService.create(any(CreateEnrollmentRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.studentId").value(1))
                .andExpect(jsonPath("$.studentName")
                        .value("Иван Иванов"))
                .andExpect(jsonPath("$.courseId").value(10))
                .andExpect(jsonPath("$.courseName")
                        .value("Основы Java"))
                .andExpect(jsonPath("$.enrollmentDate")
                        .value("2026-07-28"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.grade").isEmpty());

        verify(enrollmentService)
                .create(any(CreateEnrollmentRequest.class));
    }

    @Test
    void shouldReturnEnrollmentsByStudentId() throws Exception {
        EnrollmentResponse response = createResponse();

        when(enrollmentService.getByStudentId(1L))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/enrollments/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].studentId").value(1))
                .andExpect(jsonPath("$[0].studentName")
                        .value("Иван Иванов"))
                .andExpect(jsonPath("$[0].courseId").value(10))
                .andExpect(jsonPath("$[0].courseName")
                        .value("Основы Java"));

        verify(enrollmentService).getByStudentId(1L);
    }

    @Test
    void shouldReturnEnrollmentsByCourseId() throws Exception {
        EnrollmentResponse response = createResponse();

        when(enrollmentService.getByCourseId(10L))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/enrollments/course/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].studentId").value(1))
                .andExpect(jsonPath("$[0].courseId").value(10))
                .andExpect(jsonPath("$[0].status")
                        .value("ACTIVE"));

        verify(enrollmentService).getByCourseId(10L);
    }

    @Test
    void shouldReturn404WhenStudentNotFound() throws Exception {
        CreateEnrollmentRequest request =
                new CreateEnrollmentRequest(999L, 10L);

        when(enrollmentService.create(any(CreateEnrollmentRequest.class)))
                .thenThrow(new StudentNotFoundException(999L));

        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Студент с идентификатором 999 не найден"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/enrollments"));
    }

    @Test
    void shouldReturn404WhenCourseNotFound() throws Exception {
        CreateEnrollmentRequest request =
                new CreateEnrollmentRequest(1L, 999L);

        when(enrollmentService.create(any(CreateEnrollmentRequest.class)))
                .thenThrow(new CourseNotFoundException(999L));

        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Курс с идентификатором 999 не найден"));
    }

    @Test
    void shouldReturn409WhenEnrollmentAlreadyExists() throws Exception {
        CreateEnrollmentRequest request =
                new CreateEnrollmentRequest(1L, 10L);

        when(enrollmentService.create(any(CreateEnrollmentRequest.class)))
                .thenThrow(
                        new EnrollmentAlreadyExistsException(1L, 10L)
                );

        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message")
                        .value("Студент 1 уже записан на курс 10"));
    }

    @Test
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        String requestJson = """
                {
                  "studentId": null,
                  "courseId": null
                }
                """;

        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void shouldDeleteEnrollment() throws Exception {
        doNothing().when(enrollmentService).delete(100L);

        mockMvc.perform(delete("/api/v1/enrollments/100"))
                .andExpect(status().isNoContent());

        verify(enrollmentService).delete(100L);
    }

    @Test
    void shouldReturn404WhenEnrollmentNotFoundDuringDelete()
            throws Exception {
        org.mockito.Mockito.doThrow(
                new EnrollmentNotFoundException(999L)
        ).when(enrollmentService).delete(999L);

        mockMvc.perform(delete("/api/v1/enrollments/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Запись на курс с идентификатором 999 не найдена"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/enrollments/999"));

        verify(enrollmentService).delete(999L);
    }

    private EnrollmentResponse createResponse() {
        return new EnrollmentResponse(
                100L,
                1L,
                "Иван Иванов",
                10L,
                "Основы Java",
                LocalDate.of(2026, 7, 28),
                EnrollmentStatus.ACTIVE,
                null
        );
    }
}
