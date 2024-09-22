package com.luv2code.component;

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

@SpringBootTest
public class ApplicationExampleTest {

    private static int count = 0;
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
    void beforeEach () {
        count = count +1;
        System.out.println("testing: " + appInfo + " which is " + appDescription +
                " version: " + appVersion + " Execution of test method " + count);

        student.setFirstname("Eric");
        student.setLastname("Ruby");
        student.setEmailAddress("Eric@Ruby.com");
        studentGrades.setMathGradeResults(new ArrayList<>(Arrays.asList(100.0, 85.0, 76.50, 91.75)));
        student.setStudentGrades(studentGrades);
    }
    @DisplayName("Add grade results for student grades")
    @Test
    public void addGradeResultsForStudentGrades() {
        assertEquals(353.25, studentGrades.addGradeResultsForSingleClass(
                student.getStudentGrades().getMathGradeResults()));
    }

    @DisplayName("Add grade results for student grades not equals")
    @Test
    public void addGradeResultsForStudentGradesAssertNotEquals() {
        assertNotEquals(0, studentGrades.addGradeResultsForSingleClass(
                student.getStudentGrades().getMathGradeResults()));
    }

    @DisplayName("is grade greater")
    @Test
    public void isGradeGreaterStudentGrades () {
        assertTrue(studentGrades.isGradeGreater(90,75), "Should be true");
    }

    @DisplayName("is grade greater false")
    @Test
    public void isGradeGreaterStudentGradesAssertFalse () {
        assertFalse(studentGrades.isGradeGreater(89,92),"should be false");
    }

    @DisplayName("check null for student grades")
    @Test
    public void checkNullForStudentGrades () {
        assertNotNull(studentGrades.checkNull(student.getStudentGrades().getMathGradeResults()), "" +
                "object should not be null");
    }

    @DisplayName("create student without grade init")
    @Test
    public void createStudentWithoutGradeInit () {
        CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);
        studentTwo.setFirstname("Chad");
        studentTwo.setLastname("Darby");
        studentTwo.setEmailAddress("chad@darby.com");

        assertNotNull(studentTwo.getFirstname());
        assertNotNull(studentTwo.getLastname());
        assertNotNull(studentTwo.getEmailAddress());
        assertNull(studentGrades.checkNull(studentTwo.getStudentGrades()));
    }

    @DisplayName("verify students are prototype")
    @Test
    public void verifyStudentsArePrototype () {
        CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);
        assertNotSame(studentTwo, student);
    }


    @DisplayName("find grade point average")
    @Test
    public void findGradePointAverage() {
        assertAll("testing all assert equals",
                () -> assertEquals(353.25, studentGrades.addGradeResultsForSingleClass(
                        student.getStudentGrades().getMathGradeResults())),
                () -> assertEquals(88.31, studentGrades.findGradePointAverage(
                        student.getStudentGrades().getMathGradeResults()))
                );
    }

    @Test
    void basicTest () {}
}
