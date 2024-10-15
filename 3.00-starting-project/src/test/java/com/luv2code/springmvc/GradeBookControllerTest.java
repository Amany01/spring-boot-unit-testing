package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.h2.command.dml.MergeUsing;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradeBookControllerTest {
    @Autowired
    private StudentDao studentDao;
    private static MockHttpServletRequest request;
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private StudentAndGradeService studentCreateServiceMock;
    @Value("${sql.script.create.student}")
    private String sqlAddStudent;
    @Value("${sql.delete.student}")
    private String sqlDeleteStudent;

    @BeforeAll
    public static void setup () {
        request = new MockHttpServletRequest();
        request.setParameter("firstname", "Amany");
        request.setParameter("lastname", "Fawzy");
        request.setParameter("emailAddress", "Amany@fawzy.com");
    }

    @BeforeEach
    public void setupDatabase () {
        jdbc.execute(sqlAddStudent);
    }

    @Test
    public void getStudentHttpRequest () throws Exception {
        CollegeStudent studentOne = new GradebookCollegeStudent("Eric",
                "Roby", "eric@roby.com");
        CollegeStudent studentTwo = new GradebookCollegeStudent("Chad",
                "Darby", "chad@darby.com");
        List<CollegeStudent> collegeStudents =
                new ArrayList<>(Arrays.asList(studentOne,studentTwo));

        when(studentCreateServiceMock.getGradeBook()).thenReturn(collegeStudents);

        assertIterableEquals(collegeStudents, studentCreateServiceMock.getGradeBook());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");
    }

    @Test
    public void createStudentHttpRequest () throws  Exception {
        CollegeStudent studentOne = new CollegeStudent("Eric", "Roby", "eric@roby.com");
        List<CollegeStudent> collegeStudentsList = new ArrayList<>(Arrays.asList(studentOne));
        when(studentCreateServiceMock.getGradeBook()).thenReturn(collegeStudentsList);
        assertIterableEquals(collegeStudentsList, studentCreateServiceMock.getGradeBook());
        MvcResult mvcResult = this.mockMvc.perform(post("/")//chose mockmvcrequestbuilder.post
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstname", request.getParameterValues("firstname"))
                .param("lastname", request.getParameterValues("lastname"))
                .param("emailAddress", request.getParameterValues("emailAddress")))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"index");

        CollegeStudent verifyStudent = studentDao
                .findByEmailAddress("Amany@fawzy.com");

        assertNotNull(verifyStudent, "student should be found");
    }

    @Test
    public void deleteStudentHttpRequest () throws Exception {
        assertTrue(studentDao.findById(1).isPresent());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/delete/student/{id}", 1))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "index");
        assertFalse(studentDao.findById(1).isPresent());
    }

    @Test
    public void deleteStudentHttpRequestErrorPage () throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/delete/student/{id}", 0))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        ModelAndViewAssert.assertViewName(mav, "error");

    }

    @AfterEach
    public void setupAfterTransaction () {
        jdbc.execute(sqlDeleteStudent);
    }










}
