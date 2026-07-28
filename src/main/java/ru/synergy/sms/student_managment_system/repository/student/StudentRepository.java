package ru.synergy.sms.student_managment_system.repository.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.synergy.sms.student_managment_system.entity.student.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Page<Student> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);

    Page<Student> findByGroupNameIgnoreCase(String groupName, Pageable pageable);

    Page<Student> findByCourseNumber(Integer courseNumber, Pageable pageable);

}