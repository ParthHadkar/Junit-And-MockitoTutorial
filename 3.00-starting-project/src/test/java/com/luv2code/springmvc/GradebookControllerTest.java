package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradesService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
class GradebookControllerTest {

    static MockHttpServletRequest request;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MockMvc mockMvc;

    @Mock
    StudentAndGradesService studentCreateServiceMock;

    @Autowired
    StudentDao studentDao;

    @Value("${sql.scripts.create.student}")
    String sqlAddStudent;

    @Value("${sql.scripts.create.math.grade}")
    String sqlAddMathGrade;

    @Value("${sql.scripts.create.science.grade}")
    String sqlAddScienceGrade;

    @Value("${sql.scripts.create.history.grade}")
    String sqlAddHistoryGrade;

    @Value("${sql.scripts.delete.student}")
    String sqlDeleteStudent;

    @Value("${sql.scripts.delete.math.grade}")
    String sqlDeleteMathGrade;

    @Value("${sql.scripts.delete.science.grade}")
    String sqlDeleteScienceGrade;

    @Value("${sql.scripts.delete.history.grade}")
    String sqlDeleteHistoryGrade;

    @BeforeEach
    void beforeEach() {
        jdbcTemplate.execute(sqlAddStudent);
        jdbcTemplate.execute(sqlAddMathGrade);
        jdbcTemplate.execute(sqlAddScienceGrade);
        jdbcTemplate.execute(sqlAddHistoryGrade);
    }

    @BeforeAll
    static void setup() {
        request = new MockHttpServletRequest();
        request.setParameter("firstname", "Eric");
        request.setParameter("lastname", "Roby");
        request.setParameter("emailAddress", "eric1.roby@luv2code_school.com");
    }

    @Test
    void getStudentsHttpRequest() throws Exception {

        CollegeStudent collegeStudent1 = new GradebookCollegeStudent("Eric", "Roby", "eric.roby@luv2code_school.com");

        CollegeStudent collegeStudent2 = new GradebookCollegeStudent("Chad", "Darby", "chad.darby@luv2code_school.com");

        List<CollegeStudent> lCollegeStudents = new ArrayList<>(Arrays.asList(collegeStudent1, collegeStudent2));

        when(studentCreateServiceMock.getGradeBook()).thenReturn(lCollegeStudents);

        assertIterableEquals(lCollegeStudents, studentCreateServiceMock.getGradeBook());

        MvcResult lMvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView lModelAndView = lMvcResult.getModelAndView();

        assertNotNull(lModelAndView);
        ModelAndViewAssert.assertViewName(lModelAndView, "index");
    }

    @Test
    void createStudentHttpRequest() throws Exception {

        CollegeStudent collegeStudent1 = new GradebookCollegeStudent("Eric", "Roby", "eric.roby@luv2code_school.com");

        List<CollegeStudent> lCollegeStudents = new ArrayList<>(List.of(collegeStudent1));

        when(studentCreateServiceMock.getGradeBook()).thenReturn(lCollegeStudents);

        assertIterableEquals(lCollegeStudents, studentCreateServiceMock.getGradeBook());

        MvcResult lMvcResult = this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstname", Objects.requireNonNull(request.getParameterValues("firstname")))
                .param("lastname", Objects.requireNonNull(request.getParameterValues("lastname")))
                .param("emailAddress", Objects.requireNonNull(request.getParameterValues("emailAddress"))))
                .andExpect(status().isOk()).andReturn();

        ModelAndView lModelAndView = lMvcResult.getModelAndView();
        assertNotNull(lModelAndView);
        ModelAndViewAssert.assertViewName(lModelAndView, "index");


        CollegeStudent lCollegeStudent = studentDao.findByEmailAddress("eric1.roby@luv2code_school.com");

        assertNotNull(lCollegeStudent, "Student not found");
    }

    @Test
    void deleteStudentHttpRequest() throws Exception {
        assertTrue(studentDao.findById(1).isPresent());
        MvcResult lMvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}", 1))
                .andExpect(status().isOk()).andReturn();
        ModelAndView lModelAndView = lMvcResult.getModelAndView();
        assertNotNull(lModelAndView);
        ModelAndViewAssert.assertViewName(lModelAndView, "index");
        assertFalse(studentDao.findById(1).isPresent());
    }

    @Test
    void deleteStudentHttpRequestErrorPage() throws Exception {
        MvcResult lMvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}", 0))
                .andExpect(status().isOk()).andReturn();
        ModelAndView lModelAndView = lMvcResult.getModelAndView();
        assertNotNull(lModelAndView);
        ModelAndViewAssert.assertViewName(lModelAndView, "error");
    }

    @AfterEach
    void setupAfterTransaction() {
        jdbcTemplate.execute(sqlDeleteStudent);
        jdbcTemplate.execute(sqlDeleteMathGrade);
        jdbcTemplate.execute(sqlDeleteScienceGrade);
        jdbcTemplate.execute(sqlDeleteHistoryGrade);
    }

}
