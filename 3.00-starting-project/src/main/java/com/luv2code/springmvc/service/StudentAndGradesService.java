package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistroryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradesService {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    @Qualifier("mathGrades")
    MathGrade mathGrade;

    @Autowired
    MathGradesDao mathGradesDao;

    @Autowired
    @Qualifier("scienceGrades")
    ScienceGrade scienceGrade;

    @Autowired
    ScienceGradesDao scienceGradesDao;

    @Autowired
    @Qualifier("historyGrades")
    HistoryGrade historyGrade;

    @Autowired
    HistroryGradesDao histroryGradesDao;

    @Autowired
    StudentGrades studentGrades;

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
            mathGradesDao.deleteByStudentId(pId);
            scienceGradesDao.deleteByStudentId(pId);
            histroryGradesDao.deleteByStudentId(pId);
        }
    }

    public Iterable<CollegeStudent> getGradeBook() {
        return studentDao.findAll();
    }

    public boolean createGrade(double grade, int studentId, String gradeType) {

        if (!checkIfStudentIsNull(studentId))
            return false;

        if (grade > 0 && grade <= 100) {
            if (gradeType.equals("math")) {
                mathGrade.setId(0);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(studentId);
                mathGradesDao.save(mathGrade);
                return true;
            }
            else if (gradeType.equals("science")) {
                scienceGrade.setId(0);
                scienceGrade.setGrade(grade);
                scienceGrade.setStudentId(studentId);
                scienceGradesDao.save(scienceGrade);
                return true;
            }
            else if (gradeType.equals("history")) {
                historyGrade.setId(0);
                historyGrade.setGrade(grade);
                historyGrade.setStudentId(studentId);
                histroryGradesDao.save(historyGrade);
                return true;
            }
        }
        return false;
    }

    public int deleteGrade(int pId, String pGradeId) {
        int studentId = 0;

        if (pGradeId.equals("math")) {
            Optional<MathGrade> lGrade = mathGradesDao.findById(pId);
            if (lGrade.isEmpty()) {
                return studentId;
            }
            studentId = lGrade.get().getStudentId();
            mathGradesDao.deleteById(studentId);
        }
        else if (pGradeId.equals("science")) {
            Optional<ScienceGrade> lGrade = scienceGradesDao.findById(pId);
            if (lGrade.isEmpty()) {
                return studentId;
            }
            studentId = lGrade.get().getStudentId();
            scienceGradesDao.deleteById(studentId);
        }
        else if (pGradeId.equals("history")) {
            Optional<HistoryGrade> lGrade = histroryGradesDao.findById(pId);
            if (lGrade.isEmpty()) {
                return studentId;
            }
            studentId = lGrade.get().getStudentId();
            histroryGradesDao.deleteById(studentId);
        }
        return studentId;
    }

    public GradebookCollegeStudent studentinformation(int pStudentId) {

        if (!checkIfStudentIsNull(pStudentId))
            return null;

        Optional<CollegeStudent> lStudent = studentDao.findById(pStudentId);

        Iterable<MathGrade> mathGrades = mathGradesDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = histroryGradesDao.findGradeByStudentId(1);

        List<Grade> mathGradesList = new ArrayList<>();
        mathGrades.forEach(mathGradesList::add);

        List<Grade> scienceGradesList = new ArrayList<>();
        scienceGrades.forEach(scienceGradesList::add);

        List<Grade> historyGradesList = new ArrayList<>();
        historyGrades.forEach(historyGradesList::add);

        studentGrades.setMathGradeResults(mathGradesList);
        studentGrades.setScienceGradeResults(scienceGradesList);
        studentGrades.setHistoryGradeResults(historyGradesList);

        CollegeStudent lCollegeStudent = lStudent.get();

        return new GradebookCollegeStudent(lCollegeStudent.getId(), lCollegeStudent.getFirstname(),
                lCollegeStudent.getLastname(), lCollegeStudent.getEmailAddress(), studentGrades);

    }
}
