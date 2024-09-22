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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class MockAnnotationTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent studentOne;

    @Autowired
    StudentGrades studentGrades;
    @MockBean
    //@Mock
    private ApplicationDao applicationDao;
    @Autowired
    //@InjectMocks
    private ApplicationService applicationService;

    @BeforeEach
    public void beforeEach () {
        studentOne.setFirstname("Eric");
        studentOne.setLastname("Roby");
        studentOne.setEmailAddress("eric@roby.com");
        studentOne.setStudentGrades(studentGrades);
    }
    @DisplayName("when & verify")
    @Test
    public void assertEqualsTestForStudentGrades () {
        when(applicationDao.addGradeResultsForSingleClass(
                studentGrades.getMathGradeResults())).thenReturn(100.00);
        assertEquals(100,applicationService.addGradeResultsForSingleClass(
                studentOne.getStudentGrades().getMathGradeResults()));
        //verify that this method was called
        verify(applicationDao).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
        // verify that this method was called 3 times during this test (we called it once, so it will fail)
        // change it to 1 to pass
        verify(applicationDao, times(1)).
                addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
    }
    @DisplayName("find gpa")
    @Test
    public void assertEqualsTestFindGpa () {
        when(applicationDao.findGradePointAverage(studentGrades.getMathGradeResults()))
                .thenReturn(88.31);
        assertEquals(88.31,applicationService.findGradePointAverage(studentOne
                .getStudentGrades().getMathGradeResults()));

    }

    @DisplayName("Not Null")
    @Test
    public void testAssertNotNull () {
        when(applicationDao.checkNull(studentGrades.getMathGradeResults()))
                .thenReturn(true);
        assertNotNull(applicationService.checkNull(studentOne.getStudentGrades()
                .getMathGradeResults()), "object should not be null");
    }

    @DisplayName("throw run time exception")
    @Test
    public void throwRunTimeError () {
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

        doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStudent);

        assertThrows(RuntimeException.class, () -> {
            applicationService.checkNull(nullStudent);
        });

        verify(applicationDao, times(1)).checkNull(nullStudent);
    }

    @DisplayName("multiple stubbing")
    @Test
    public void stubbingConsecutiveCalls () {
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

        when(applicationDao.checkNull(nullStudent))
                .thenThrow(new RuntimeException())
                .thenReturn("Do not throw exception second time");

        assertThrows(RuntimeException.class, () -> {
            applicationService.checkNull(nullStudent);
        });

        assertEquals("Do not throw exception second time", applicationService.checkNull(nullStudent));

        verify(applicationDao, times(2)).checkNull(nullStudent);
    }













}
