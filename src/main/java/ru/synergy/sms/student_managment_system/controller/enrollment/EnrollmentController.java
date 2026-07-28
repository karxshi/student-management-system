package ru.synergy.sms.student_managment_system.controller.enrollment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.synergy.sms.student_managment_system.dto.enrollment.CreateEnrollmentRequest;
import ru.synergy.sms.student_managment_system.dto.enrollment.EnrollmentResponse;
import ru.synergy.sms.student_managment_system.dto.enrollment.UpdateGradeRequest;
import ru.synergy.sms.student_managment_system.service.enrollment.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
@Tag(
        name = "Записи на курсы",
        description = "API для управления записями студентов на учебные курсы"
)
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Operation(summary = "Записать студента на курс")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Студент успешно записан на курс"),
            @ApiResponse(responseCode = "404", description = "Студент или курс не найден"),
            @ApiResponse(responseCode = "409", description = "Студент уже записан на курс")
    })
    @PostMapping
    public ResponseEntity<EnrollmentResponse> create(
            @Valid @RequestBody CreateEnrollmentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.create(request));
    }

    @Operation(summary = "Получить список курсов студента")
    @ApiResponse(responseCode = "200", description = "Список курсов успешно получен")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentResponse>> getByStudent(
            @Parameter(description = "Идентификатор студента", example = "1")
            @PathVariable Long studentId
    ) {
        return ResponseEntity.ok(
                enrollmentService.getByStudentId(studentId)
        );
    }

    @Operation(summary = "Получить список студентов курса")
    @ApiResponse(responseCode = "200", description = "Список студентов успешно получен")
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentResponse>> getByCourse(
            @Parameter(description = "Идентификатор курса", example = "10")
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                enrollmentService.getByCourseId(courseId)
        );
    }

    @Operation(summary = "Удалить запись студента на курс")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Запись успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Запись не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Идентификатор записи", example = "5")
            @PathVariable Long id
    ) {
        enrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Выставить оценку за курс")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Оценка успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Запись не найдена")
    })
    @PatchMapping("/{id}/grade")
    public ResponseEntity<EnrollmentResponse> updateGrade(
            @Parameter(description = "Идентификатор записи", example = "5")
            @PathVariable Long id,
            @Valid @RequestBody UpdateGradeRequest request
    ) {
        return ResponseEntity.ok(
                enrollmentService.updateGrade(id, request)
        );
    }
}
