package ru.synergy.sms.student_managment_system.controller.course;

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
import org.springframework.web.bind.annotation.RestController;
import ru.synergy.sms.student_managment_system.dto.course.CourseResponse;
import ru.synergy.sms.student_managment_system.dto.course.CreateCourseRequest;
import ru.synergy.sms.student_managment_system.dto.course.UpdateCourseRequest;
import ru.synergy.sms.student_managment_system.service.course.CourseService;

import java.net.URI;
import java.util.List;

@Tag(
        name = "Курсы",
        description = "Управление учебными курсами"
)
@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Создать курс")
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
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    @Operation(summary = "Получить список курсов")
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAll() {
        return ResponseEntity.ok(courseService.getAll());
    }

    @Operation(summary = "Обновить курс")
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCourseRequest request
    ) {
        return ResponseEntity.ok(courseService.update(id, request));
    }

    @Operation(summary = "Удалить курс")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
