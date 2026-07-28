package ru.synergy.sms.student_managment_system.service.student;

import ru.synergy.sms.student_managment_system.dto.student.CreateStudentRequest;
import ru.synergy.sms.student_managment_system.dto.student.PageResponse;
import ru.synergy.sms.student_managment_system.dto.student.StudentResponse;
import ru.synergy.sms.student_managment_system.dto.student.UpdateStudentRequest;

public interface StudentService {
    StudentResponse create(CreateStudentRequest request);

    StudentResponse getById(Long id);
    void delete(Long id);

    StudentResponse update(Long id, UpdateStudentRequest request);

    PageResponse<StudentResponse> getAll(int page, int size, String sort);

    PageResponse<StudentResponse> searchByLastName(
            String lastName,
            int page,
            int size
    );

    PageResponse<StudentResponse> searchByGroup(
            String groupName,
            int page,
            int size
    );

    PageResponse<StudentResponse> searchByCourseNumber(
            Integer courseNumber,
            int page,
            int size
    );
}
