package ru.synergy.sms.student_managment_system.controller.student;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
        description = "API для управления данными студентов"
)
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Создать студента")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Студент успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "409", description = "Студент с таким email уже существует")
    })
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Студент найден"),
            @ApiResponse(responseCode = "404", description = "Студент не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getById(
            @Parameter(description = "Идентификатор студента", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(studentService.getById(id));
    }

    @Operation(summary = "Получить список студентов")
    @ApiResponse(responseCode = "200", description = "Список студентов успешно получен")
    @GetMapping
    public ResponseEntity<PageResponse<StudentResponse>> getAll(
            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Поле сортировки", example = "id")
            @RequestParam(defaultValue = "id") String sort
    ) {
        return ResponseEntity.ok(
                studentService.getAll(page, size, sort)
        );
    }

    @Operation(summary = "Удалить студента")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Студент успешно удален"),
            @ApiResponse(responseCode = "404", description = "Студент не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Идентификатор студента", example = "1")
            @PathVariable Long id
    ) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Обновить данные студента")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные студента успешно обновлены"),
            @ApiResponse(responseCode = "404", description = "Студент не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> update(
            @Parameter(description = "Идентификатор студента", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateStudentRequest request
    ) {
        return ResponseEntity.ok(studentService.update(id, request));
    }

    @Operation(summary = "Найти студентов по фамилии")
    @ApiResponse(responseCode = "200", description = "Поиск выполнен успешно")
    @GetMapping("/search/by-last-name")
    public ResponseEntity<PageResponse<StudentResponse>> searchByLastName(
            @Parameter(description = "Фамилия студента", example = "Иванов")
            @RequestParam String lastName,

            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                studentService.searchByLastName(lastName, page, size)
        );
    }

    @Operation(summary = "Найти студентов по учебной группе")
    @ApiResponse(responseCode = "200", description = "Поиск выполнен успешно")
    @GetMapping("/search/by-group")
    public ResponseEntity<PageResponse<StudentResponse>> searchByGroup(
            @Parameter(description = "Название учебной группы", example = "ИВТ-101")
            @RequestParam String groupName,

            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                studentService.searchByGroup(groupName, page, size)
        );
    }

    @Operation(summary = "Найти студентов по номеру курса")
    @ApiResponse(responseCode = "200", description = "Поиск выполнен успешно")
    @GetMapping("/search/by-course")
    public ResponseEntity<PageResponse<StudentResponse>> searchByCourseNumber(
            @Parameter(description = "Номер курса обучения", example = "2")
            @RequestParam Integer courseNumber,

            @Parameter(description = "Номер страницы", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Размер страницы", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                studentService.searchByCourseNumber(courseNumber, page, size)
        );
    }
}