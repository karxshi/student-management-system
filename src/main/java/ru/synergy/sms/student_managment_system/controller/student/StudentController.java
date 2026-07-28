package ru.synergy.sms.student_managment_system.controller.student;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.synergy.sms.student_managment_system.dto.student.CreateStudentRequest;
import ru.synergy.sms.student_managment_system.dto.student.PageResponse;
import ru.synergy.sms.student_managment_system.dto.student.StudentResponse;
import ru.synergy.sms.student_managment_system.dto.student.UpdateStudentRequest;
import ru.synergy.sms.student_managment_system.service.student.StudentService;

import java.net.URI;

@Tag(
        name = "Студенты",
        description = "Управление данными студентов"
)
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Создать студента")
    @PostMapping
    public ResponseEntity<StudentResponse> create(
            @Valid @RequestBody CreateStudentRequest request
    ) {
        StudentResponse response = studentService.create(request);

        return ResponseEntity
                .created(URI.create("/api/v1/students/" + response.id()))
                .body(response);
    }

    @Operation(summary = "Получить студента по идентификатору")
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getById(id));
    }

    @Operation(summary = "Получить список студентов")
    @GetMapping
    public ResponseEntity<PageResponse<StudentResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        return ResponseEntity.ok(
                studentService.getAll(page, size, sort)
        );
    }

    @Operation(summary = "Удалить студента")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновить данные студента")
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStudentRequest request
    ) {
        return ResponseEntity.ok(studentService.update(id, request));
    }

    @Operation(summary = "Найти студентов по фамилии")
    @GetMapping("/search/by-last-name")
    public ResponseEntity<PageResponse<StudentResponse>> searchByLastName(
            @RequestParam String lastName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                studentService.searchByLastName(lastName, page, size)
        );
    }

    @Operation(summary = "Найти студентов по группе")
    @GetMapping("/search/by-group")
    public ResponseEntity<PageResponse<StudentResponse>> searchByGroup(
            @RequestParam String groupName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                studentService.searchByGroup(groupName, page, size)
        );
    }

    @Operation(summary = "Найти студентов по курсу")
    @GetMapping("/search/by-course")
    public ResponseEntity<PageResponse<StudentResponse>> searchByCourseNumber(
            @RequestParam Integer courseNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                studentService.searchByCourseNumber(courseNumber, page, size)
        );
    }
}
