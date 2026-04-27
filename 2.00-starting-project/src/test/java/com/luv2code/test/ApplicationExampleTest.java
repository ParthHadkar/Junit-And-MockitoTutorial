package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = MvcTestingExampleApplication.class)
class ApplicationExampleTest {

    private int count = 0;

    @Value("${info.app.name}")
    private String appInfo;

    @Value("${info.app.description}")
    private String appDescription;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.school.name}")
    private String schoolName;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades studentGrades;

    @Autowired
    ApplicationContext context;

    @BeforeEach
    public void beforeEach() {
        count += 1;
        System.out.println("Testing: "+ appInfo + " which is "+appDescription +
                " Version "+ appVersion + ". Execution of test method "+count + " School Name is "+schoolName);
        student.setFirstname("Eric");
        student.setLastname("Roby");
        student.setEmailAddress("erio.rody@luv2code_school.com");
        studentGrades.setMathGradeResults(new ArrayList<>(Arrays.asList(100.0, 85.0, 76.50, 91.75)));
        student.setStudentGrades(studentGrades);
    }

    @Test
    @DisplayName("Add grade results for student grades")
    void addGradeResultForStudentGrades() {
        assertEquals(353.25, studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults()));
    }

    @Test
    @DisplayName("Add grade results for student grades not equal")
    void addGradeResultForStudentGradesNotEquals() {
        assertNotEquals(0, studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults()));
    }

    @Test
    @DisplayName("Is grade greater")
    void isGradeGreaterStudentGrades() {
        assertTrue(studentGrades.isGradeGreater(90, 75), "failure - should be true");
    }

    @Test
    @DisplayName("Is grade greater false")
    void isGradeGreaterStudentGradesAssertFalse() {
            assertFalse(studentGrades.isGradeGreater(89, 92), "failure - should be false");
    }

    @Test
    @DisplayName("Check Null For Student Grades")
    void checkNullForStudentGrades() {
        assertNotNull(student.getStudentGrades().getMathGradeResults(), "Object should not be null");
    }

    @Test
    @DisplayName("Create student without grade init")
    void createStudentWithoutGradeInit() {
        CollegeStudent lCollegeStudent = context.getBean("collegeStudent", CollegeStudent.class);
        lCollegeStudent.setFirstname("Test");
        lCollegeStudent.setLastname("Test");
        lCollegeStudent.setEmailAddress("Test@gmail.com");
        assertNotNull(lCollegeStudent.getFirstname());
        assertNotNull(lCollegeStudent.getLastname());
        assertNotNull(lCollegeStudent.getEmailAddress());
        assertNull(studentGrades.checkNull(lCollegeStudent.getStudentGrades()));
    }

    @Test
    @DisplayName("Verify student are prototypes")
    void verifyStudentAreprototypes() {
        CollegeStudent lCollegeStudent = context.getBean("collegeStudent", CollegeStudent.class);
        assertNotSame(student, lCollegeStudent);
    }

    @Test
    @DisplayName("Find Grade Point Average")
    void findGradePointAverage() {
        assertAll("Testing all assertEquals",
                () -> assertEquals(353.25, studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults())),
                () -> assertEquals(88.31, studentGrades.findGradePointAverage(
                        student.getStudentGrades().getMathGradeResults()))
        );
    }

}
