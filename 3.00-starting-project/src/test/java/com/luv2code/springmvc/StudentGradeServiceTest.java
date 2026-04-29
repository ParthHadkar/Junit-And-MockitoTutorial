package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
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

    @BeforeEach
    void setupDatabase() {
        jdbcTemplate.execute("INSERT INTO student(firstname, lastName, email_address) " +
                "Values ('Eric', 'Roby', 'eric.roby@luv2code_school.com')");
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

        assertTrue(lDeleteCollegeStudent.isPresent(), "return True");

        studentService.deleteStudent(1);

        lDeleteCollegeStudent =  studentDao.findById(1);

        assertFalse(lDeleteCollegeStudent.isPresent(), "return False");

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

    @AfterEach
    void setupAfterTransaction() {
        jdbcTemplate.execute("DELETE FROM student");
        jdbcTemplate.execute("ALTER TABLE student ALTER COLUMN ID RESTART WITH 1");
    }

}
