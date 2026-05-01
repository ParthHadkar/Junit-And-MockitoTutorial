package com.luv2code.springmvc;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.repository.HistroryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
class StudentGradeServiceTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private StudentAndGradesService studentService;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    MathGradesDao mathGradesDao;

    @Autowired
    ScienceGradesDao scienceGradesDao;

    @Autowired
    HistroryGradesDao histroryGradesDao;

    @BeforeEach
    void setupDatabase() {
        jdbcTemplate.execute("INSERT INTO student(firstname, lastName, email_address) " +
                "Values ('Eric', 'Roby', 'eric.roby@luv2code_school.com')");

        jdbcTemplate.execute("INSERT INTO math_grade(student_id, grade) Values (1,100.00)");
        jdbcTemplate.execute("INSERT INTO science_grade(student_id, grade) Values (1,100.00)");
        jdbcTemplate.execute("INSERT INTO history_grade(student_id, grade) Values (1,100.00)");
    }

    @Test
    void createStudentServices() {

        studentService.createStudent("Chad", "Darby", "chad.darby@luv2code_school.com");

        CollegeStudent lCollegeStudent = studentDao.findByEmailAddress("chad.darby@luv2code_school.com");

        assertEquals("chad.darby@luv2code_school.com", lCollegeStudent.getEmailAddress(), " find by email");
    }

    @Test
    void isStudentNullCheck() {
        assertTrue(studentService.checkIfStudentIsNull(1));

        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @Test
    void deleteStudentService() {
        Optional<CollegeStudent> lDeleteCollegeStudent =  studentDao.findById(1);
        Optional<MathGrade> lDeleteMathGrade =  mathGradesDao.findById(1);
        Optional<ScienceGrade> lDeleteScienceGrade =  scienceGradesDao.findById(1);
        Optional<HistoryGrade> lDeleteHistoryGrade =  histroryGradesDao.findById(1);

        assertTrue(lDeleteCollegeStudent.isPresent(), "return True");
        assertTrue(lDeleteMathGrade.isPresent(), "return True");
        assertTrue(lDeleteScienceGrade.isPresent(), "return True");
        assertTrue(lDeleteHistoryGrade.isPresent(), "return True");

        studentService.deleteStudent(1);

        lDeleteCollegeStudent =  studentDao.findById(1);
        lDeleteMathGrade =  mathGradesDao.findById(1);
        lDeleteScienceGrade =  scienceGradesDao.findById(1);
        lDeleteHistoryGrade =  histroryGradesDao.findById(1);

        assertFalse(lDeleteCollegeStudent.isPresent(), "return False");
        assertFalse(lDeleteMathGrade.isPresent(), "return False");
        assertFalse(lDeleteScienceGrade.isPresent(), "return False");
        assertFalse(lDeleteHistoryGrade.isPresent(), "return False");

    }

    @Test
    @Sql("/insertData.sql")
    void getGradeBookService() {
        Iterable<CollegeStudent> lCollegeStudentIterable = studentService.getGradeBook();

        List<CollegeStudent> lCollegeStudents = new ArrayList<CollegeStudent>();

        for (CollegeStudent lCollegeStudent : lCollegeStudentIterable) {
            lCollegeStudents.add(lCollegeStudent);
        }

        assertEquals(5, lCollegeStudents.size());
    }

    @Test
    public void createGradeService() {

        // create the grade
        assertTrue(studentService.createGrade(80.50, 1, "math"));
        assertTrue(studentService.createGrade(80.50, 1, "science"));
        assertTrue(studentService.createGrade(80.50, 1, "history"));


        // get all grades with studentId
        Iterable<MathGrade> mathGrades = mathGradesDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = histroryGradesDao.findGradeByStudentId(1);


        // verify there is grades
        assertTrue(((Collection<MathGrade>) mathGrades).size() == 2, "Student has math grades");
        assertTrue(((Collection<ScienceGrade>) scienceGrades).size() == 2, "Student has science grades");
        assertTrue(((Collection<HistoryGrade>) historyGrades).size() == 2, "Student has history grades");

    }

    @Test
    public void createGradeServiceReturnFalse() {
        assertFalse(studentService.createGrade(105, 1, "math"));
        assertFalse(studentService.createGrade(-5, 1, "math"));
        assertFalse(studentService.createGrade(80.50, 2, "math"));
        assertFalse(studentService.createGrade(80.50, 1, "literature"));
    }

    @Test
    public void deleteGradeService() {
        assertEquals(1, studentService.deleteGrade(1, "math"), "Return student id after delete");
        assertEquals(1, studentService.deleteGrade(1, "science"), "Return student id after delete");
        assertEquals(1, studentService.deleteGrade(1, "history"), "Return student id after delete");
    }

    @Test
    public void deleteGradeServiceReturnIdOfZero() {
        assertEquals(0, studentService.deleteGrade(0, "math"), "No student have 0 id");
        assertEquals(0, studentService.deleteGrade(1, "literature"), "No student have literature class");
    }

    @Test
    public void studentInformationService() {

        GradebookCollegeStudent lGradebookCollegeStudent = studentService.studentinformation(1);
        assertEquals(1, lGradebookCollegeStudent.getId());
        assertEquals("Eric", lGradebookCollegeStudent.getFirstname());
        assertEquals("Roby", lGradebookCollegeStudent.getLastname());
        assertEquals("eric.roby@luv2code_school.com", lGradebookCollegeStudent.getEmailAddress());
        assertTrue(lGradebookCollegeStudent.getStudentGrades().getMathGradeResults().size() == 1);
        assertTrue(lGradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size() == 1);
        assertTrue(lGradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size() == 1);

    }

    @Test
    public void studentInformationServiceReturnNull() {

        GradebookCollegeStudent lGradebookCollegeStudent = studentService.studentinformation(0);

        assertNull(lGradebookCollegeStudent);
    }

    @AfterEach
    void setupAfterTransaction() {
        jdbcTemplate.execute("DELETE FROM student");
        jdbcTemplate.execute("DELETE FROM math_grade");
        jdbcTemplate.execute("DELETE FROM science_grade");
        jdbcTemplate.execute("DELETE FROM history_grade");

        jdbcTemplate.execute("ALTER TABLE student ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE math_grade ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE science_grade ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE history_grade ALTER COLUMN ID RESTART WITH 1");
    }

}
