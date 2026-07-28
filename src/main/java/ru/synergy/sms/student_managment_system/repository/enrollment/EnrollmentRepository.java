package ru.synergy.sms.student_managment_system.repository.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.synergy.sms.student_managment_system.entity.enrollment.Enrollment;

import java.util.List;

public interface EnrollmentRepository
        extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentIdAndCourseId(
            Long studentId,
            Long courseId
    );

    List<Enrollment> findAllByStudentId(Long studentId);

    List<Enrollment> findAllByCourseId(Long courseId);
}
