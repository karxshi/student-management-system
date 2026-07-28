package ru.synergy.sms.student_managment_system.controller.course;

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
import org.springframework.web.bind.annotation.RestController;
import ru.synergy.sms.student_managment_system.dto.course.CourseResponse;
import ru.synergy.sms.student_managment_system.dto.course.CreateCourseRequest;
import ru.synergy.sms.student_managment_system.dto.course.UpdateCourseRequest;
import ru.synergy.sms.student_managment_system.service.course.CourseService;

import java.net.URI;
import java.util.List;

@Tag(
        name = "Курсы",
        description = "API для управления учебными курсами"
)
@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Создать учебный курс")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Курс успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "409", description = "Курс уже существует")
    })
    @PostMapping
    public ResponseEntity<CourseResponse> create(
            @Valid @RequestBody CreateCourseRequest request
    ) {
        CourseResponse response = courseService.create(request);

        return ResponseEntity
                .created(URI.create("/api/v1/courses/" + response.id()))
                .body(response);
    }

    @Operation(summary = "Получить курс по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс найден"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getById(
            @Parameter(description = "Идентификатор курса", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    @Operation(summary = "Получить список всех курсов")
    @ApiResponse(responseCode = "200", description = "Список курсов успешно получен")
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAll() {
        return ResponseEntity.ok(courseService.getAll());
    }

    @Operation(summary = "Обновить учебный курс")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> update(
            @Parameter(description = "Идентификатор курса", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateCourseRequest request
    ) {
        return ResponseEntity.ok(courseService.update(id, request));
    }

    @Operation(summary = "Удалить учебный курс")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Курс успешно удален"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Идентификатор курса", example = "1")
            @PathVariable Long id
    ) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
