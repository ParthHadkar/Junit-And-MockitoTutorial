package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
class MockAnnotationTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent studentOne;

    @Autowired
    StudentGrades studentGrades;

    //@Mock
    @MockitoBean
    private ApplicationDao applicationDao;

    //@InjectMocks
    @Autowired
    private ApplicationService applicationService;

    @BeforeEach
    public void beforeEach() {
        studentOne.setFirstname("Eric");
        studentOne.setLastname("Roby");
        studentOne.setEmailAddress("erio.rody@luv2code_school.com");
        studentOne.setStudentGrades(studentGrades);
    }

    @Test
    @DisplayName("When & verify")
    void assertEqualsTestAddGrades() {
        // setup
        when(applicationDao.addGradeResultsForSingleClass(studentGrades.getMathGradeResults())).thenReturn(100.00);

        // execute and assert
        assertEquals(100, applicationDao.addGradeResultsForSingleClass(studentGrades.getMathGradeResults()));

        verify(applicationDao).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());

        verify(applicationDao, times(1)).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
    }

    @Test
    @DisplayName("Find Gpa")
    void assertEqualsTestfindGpa() {
        // setup
        when(applicationDao.findGradePointAverage(studentGrades.getMathGradeResults())).thenReturn(88.31);

        // execute and assert
        assertEquals(88.31, applicationDao.findGradePointAverage(studentGrades.getMathGradeResults()));

        verify(applicationDao).findGradePointAverage(studentGrades.getMathGradeResults());

    }

    @Test
    @DisplayName("Not Null")
    void testAssertNotNull() {
        // setup
        when(applicationDao.checkNull(studentGrades.getMathGradeResults())).thenReturn(true);

        // execute and assert
        assertNotNull(applicationDao.checkNull(studentGrades.getMathGradeResults()),
                "Object should not be null");

        verify(applicationDao).checkNull(studentGrades.getMathGradeResults());

        verify(applicationDao, times(1)).checkNull(studentGrades.getMathGradeResults());
    }

    @Test
    @DisplayName("Throw runtime error")
    void throwRuntimeError() {
        // setup
        CollegeStudent lCollegeStudent = (CollegeStudent) context.getBean("collegeStudent");

        doThrow(new RuntimeException()).when(applicationDao).checkNull(lCollegeStudent);

        // execute and assert
        assertThrows(RuntimeException.class, () -> { applicationDao.checkNull(lCollegeStudent); });

        verify(applicationDao, times(1)).checkNull(lCollegeStudent);
    }


    @Test
    @DisplayName("Multiple Stubbing")
    void stubbingConsecutiveCalls() {
        // setup
        CollegeStudent lCollegeStudent = (CollegeStudent) context.getBean("collegeStudent");

        when(applicationDao.checkNull(lCollegeStudent))
                .thenThrow(new RuntimeException())
                .thenReturn("Do not Throw exception for second time");

        // execute and assert
        assertThrows(RuntimeException.class, () -> { applicationDao.checkNull(lCollegeStudent); });

        assertEquals("Do not Throw exception for second time", applicationDao.checkNull(lCollegeStudent));

        verify(applicationDao, times(2)).checkNull(lCollegeStudent);
    }
}
