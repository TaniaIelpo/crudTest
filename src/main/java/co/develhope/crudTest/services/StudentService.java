package co.develhope.crudTest.services;

import co.develhope.crudTest.entities.Student;
import co.develhope.crudTest.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Tania Ielpo
 */
@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    public Student setStudentIsWorkingStatus(Long id, boolean isWorking) {
        Optional<Student> student = studentRepository.findById(id);
        if (!student.isPresent()) return null;
        student.get().setWorking(isWorking);
        return studentRepository.save(student.get());
    }
}
