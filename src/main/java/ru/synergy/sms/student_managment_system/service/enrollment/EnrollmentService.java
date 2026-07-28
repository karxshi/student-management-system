package ru.synergy.sms.student_managment_system.service.enrollment;

import ru.synergy.sms.student_managment_system.dto.enrollment.CreateEnrollmentRequest;
import ru.synergy.sms.student_managment_system.dto.enrollment.EnrollmentResponse;
import ru.synergy.sms.student_managment_system.dto.enrollment.UpdateGradeRequest;

import java.util.List;

public interface EnrollmentService {

    EnrollmentResponse create(CreateEnrollmentRequest request);

    List<EnrollmentResponse> getByStudentId(Long studentId);

    List<EnrollmentResponse> getByCourseId(Long courseId);

    void delete(Long id);

    EnrollmentResponse updateGrade(Long enrollmentId, UpdateGradeRequest request);
}
