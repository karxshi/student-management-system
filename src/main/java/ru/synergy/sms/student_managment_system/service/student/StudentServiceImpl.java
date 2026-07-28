package ru.synergy.sms.student_managment_system.service.student;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.synergy.sms.student_managment_system.dto.student.CreateStudentRequest;
import ru.synergy.sms.student_managment_system.dto.student.PageResponse;
import ru.synergy.sms.student_managment_system.dto.student.StudentResponse;
import ru.synergy.sms.student_managment_system.dto.student.UpdateStudentRequest;
import ru.synergy.sms.student_managment_system.entity.student.Student;
import ru.synergy.sms.student_managment_system.exception.student.EmailAlreadyExistsException;
import ru.synergy.sms.student_managment_system.exception.student.StudentNotFoundException;
import ru.synergy.sms.student_managment_system.mapper.student.StudentMapper;
import ru.synergy.sms.student_managment_system.repository.student.StudentRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public StudentResponse create(CreateStudentRequest request) {
        if (studentRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        Student student = studentMapper.toEntity(request);
        Student savedStudent = studentRepository.save(student);

        return studentMapper.toResponse(savedStudent);
    }

    @Override
    public StudentResponse getById(Long id) {
        Student student = findStudentById(id);
        return studentMapper.toResponse(student);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Student student = findStudentById(id);
        studentRepository.delete(student);
    }

    private Student findStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    @Transactional
    public StudentResponse update(
            Long id,
            UpdateStudentRequest request
    ) {
        Student student = findStudentById(id);

        if (studentRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new EmailAlreadyExistsException(request.email());
        }

        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());
        student.setGroupName(request.groupName());
        student.setCourseNumber(request.courseNumber());

        Student updatedStudent = studentRepository.save(student);

        return studentMapper.toResponse(updatedStudent);
    }

    @Override
    public PageResponse<StudentResponse> getAll(
            int page,
            int size,
            String sort
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort).ascending()
        );

        Page<Student> students = studentRepository.findAll(pageable);

        return toPageResponse(students);
    }

    @Override
    public PageResponse<StudentResponse> searchByLastName(
            String lastName,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Student> students =
                studentRepository.findByLastNameContainingIgnoreCase(
                        lastName,
                        pageable
                );

        return toPageResponse(students);
    }

    @Override
    public PageResponse<StudentResponse> searchByGroup(
            String groupName,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Student> students =
                studentRepository.findByGroupNameIgnoreCase(
                        groupName,
                        pageable
                );

        return toPageResponse(students);
    }

    @Override
    public PageResponse<StudentResponse> searchByCourseNumber(
            Integer courseNumber,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Student> students =
                studentRepository.findByCourseNumber(courseNumber, pageable);

        return toPageResponse(students);
    }

    private PageResponse<StudentResponse> toPageResponse(
            Page<Student> students
    ) {
        List<StudentResponse> content = students.getContent()
                .stream()
                .map(studentMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                students.getNumber(),
                students.getSize(),
                students.getTotalElements(),
                students.getTotalPages(),
                students.isFirst(),
                students.isLast()
        );
    }

}
