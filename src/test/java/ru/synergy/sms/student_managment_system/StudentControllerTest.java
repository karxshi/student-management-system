package ru.synergy.sms.student_managment_system;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.synergy.sms.student_managment_system.controller.student.StudentController;
import ru.synergy.sms.student_managment_system.dto.student.CreateStudentRequest;
import ru.synergy.sms.student_managment_system.dto.student.StudentResponse;
import ru.synergy.sms.student_managment_system.dto.student.UpdateStudentRequest;
import ru.synergy.sms.student_managment_system.exception.student.EmailAlreadyExistsException;
import ru.synergy.sms.student_managment_system.exception.GlobalExceptionHandler;
import ru.synergy.sms.student_managment_system.exception.student.StudentNotFoundException;
import ru.synergy.sms.student_managment_system.service.student.StudentService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Import(GlobalExceptionHandler.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private StudentService studentService;

    @Test
    void shouldCreateStudent() throws Exception {
        CreateStudentRequest request = new CreateStudentRequest(
                "Иван",
                "Иванов",
                "ivanov@example.com",
                "ИВТ-101",
                1
        );

        StudentResponse response = new StudentResponse(
                1L,
                "Иван",
                "Иванов",
                "ivanov@example.com",
                "ИВТ-101",
                1
        );

        when(studentService.create(any(CreateStudentRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        "/api/v1/students/1"
                ))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.lastName").value("Иванов"))
                .andExpect(jsonPath("$.email")
                        .value("ivanov@example.com"))
                .andExpect(jsonPath("$.groupName").value("ИВТ-101"))
                .andExpect(jsonPath("$.courseNumber").value(1));

        verify(studentService).create(any(CreateStudentRequest.class));
    }

    @Test
    void shouldReturnStudentById() throws Exception {
        StudentResponse response = new StudentResponse(
                1L,
                "Иван",
                "Иванов",
                "ivanov@example.com",
                "ИВТ-101",
                1
        );

        when(studentService.getById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.lastName").value("Иванов"))
                .andExpect(jsonPath("$.email")
                        .value("ivanov@example.com"))
                .andExpect(jsonPath("$.groupName").value("ИВТ-101"))
                .andExpect(jsonPath("$.courseNumber").value(1));

        verify(studentService).getById(1L);
    }

    @Test
    void shouldReturn404WhenStudentNotFound() throws Exception {
        when(studentService.getById(999L))
                .thenThrow(new StudentNotFoundException(999L));

        mockMvc.perform(get("/api/v1/students/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Студент с идентификатором 999 не найден"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/students/999"));
    }

    @Test
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        CreateStudentRequest request = new CreateStudentRequest(
                "",
                "",
                "incorrect-email",
                "",
                1
        );

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {
        CreateStudentRequest request = new CreateStudentRequest(
                "Иван",
                "Иванов",
                "ivanov@example.com",
                "ИВТ-101",
                1
        );

        when(studentService.create(any(CreateStudentRequest.class)))
                .thenThrow(
                        new EmailAlreadyExistsException(
                                "ivanov@example.com"
                        )
                );

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message")
                        .value(
                                "Студент с email ivanov@example.com уже существует"
                        ));
    }

    @Test
    void shouldUpdateStudent() throws Exception {
        UpdateStudentRequest request = new UpdateStudentRequest(
                "Петр",
                "Петров",
                "petrov@example.com",
                "ИВТ-201",
                2
        );

        StudentResponse response = new StudentResponse(
                1L,
                "Петр",
                "Петров",
                "petrov@example.com",
                "ИВТ-201",
                2
        );

        when(studentService.update(
                eq(1L),
                any(UpdateStudentRequest.class)
        )).thenReturn(response);

        mockMvc.perform(put("/api/v1/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Петр"))
                .andExpect(jsonPath("$.lastName").value("Петров"))
                .andExpect(jsonPath("$.email")
                        .value("petrov@example.com"))
                .andExpect(jsonPath("$.groupName").value("ИВТ-201"))
                .andExpect(jsonPath("$.courseNumber").value(2));

        verify(studentService).update(
                eq(1L),
                any(UpdateStudentRequest.class)
        );
    }

    @Test
    void shouldDeleteStudent() throws Exception {
        doNothing().when(studentService).delete(1L);

        mockMvc.perform(delete("/api/v1/students/1"))
                .andExpect(status().isNoContent());

        verify(studentService).delete(1L);
    }
}
