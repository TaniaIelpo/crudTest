package co.develhope.crudTest.controllers;


import co.develhope.crudTest.entities.Student;
import co.develhope.crudTest.repositories.StudentRepository;
import co.develhope.crudTest.services.StudentService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Tania Ielpo
 */

@Controller
@RequestMapping("/student")
public class StudentController {


    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;


    @PostMapping("")
    public @ResponseBody
    Student create(@RequestBody Student student){
        return studentRepository.save(student);
    }


    @GetMapping("/")
    public @ResponseBody
    List<Student> getStudents(){
        return studentRepository.findAll();
    }


    @GetMapping("/{id}")
    public @ResponseBody  Student getAStudent(@PathVariable long id){
        Optional<Student> student =  studentRepository.findById(id);
        if(student.isPresent()){
            return student.get();
        }else{
            return null;
        }//it can be null
    }


    @PutMapping("/{id}")
    public @ResponseBody Student updateID(@PathVariable long id, @RequestBody  @NotNull Student student){
        student.setId(id);
        return studentRepository.save(student);
    }


    @PutMapping("/{id}/work")
    public @ResponseBody Student setIsWorking(@PathVariable long id, @RequestParam ("working") boolean working){
        return studentService.setStudentIsWorkingStatus(id, working);
    }


    @DeleteMapping("/{id}")
    public @ResponseBody void delete(@PathVariable Long id){
        studentRepository.deleteById(id);
    }


}
