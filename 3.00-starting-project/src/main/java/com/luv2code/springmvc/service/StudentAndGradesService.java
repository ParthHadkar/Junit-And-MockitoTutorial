package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class StudentAndGradesService {

    @Autowired
    private StudentDao studentDao;

    public void createStudent(String pFirstName, String pLastName, String pEmailAddress) {
        CollegeStudent lCollegeStudent = new CollegeStudent(pFirstName, pLastName, pEmailAddress);
        lCollegeStudent.setId(0);
        studentDao.save(lCollegeStudent);
    }

    public boolean checkIfStudentIsNull(int pId) {
        Optional<CollegeStudent> lCollegeStudent = studentDao.findById(pId);
        return  lCollegeStudent.isPresent();
    }

    public void deleteStudent(int pId) {
        if (checkIfStudentIsNull(pId)) {
            studentDao.deleteById(pId);
        }
    }

    public Iterable<CollegeStudent> getGradeBook() {
        return studentDao.findAll();
    }
}
