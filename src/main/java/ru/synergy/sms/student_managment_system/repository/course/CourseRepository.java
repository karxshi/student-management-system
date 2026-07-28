package ru.synergy.sms.student_managment_system.repository.course;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.synergy.sms.student_managment_system.entity.course.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

}
