package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
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
public class StudentAndGradeServiceTest {

    @Autowired
    private StudentAndGradeService studentService;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private MathGradesDao mathGradesDao;
    private MathGrade mathGrade;

    @BeforeEach
    public void setupDatabase () {
        jdbc.execute("INSERT INTO student(id, firstname, lastname, email_address)" +
                "VALUES (1, 'Eric', 'Roby', 'eric@roby.com')");
    }

    @Test
    public void createStudentService() {
        studentService.createStudent("Amany", "Fawzy", "amany@fawzy.com");
        CollegeStudent student = studentDao.findByEmailAddress("amany@fawzy.com");
        assertEquals("amany@fawzy.com", student.getEmailAddress(), "find by email");
    }

    @Test
    public void isStudentNullCheck () {
        assertTrue(studentService.checkIfStudentIsNull(1));
        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @Test
    public void deleteStudentService () {
        Optional<CollegeStudent> deleteCollegeStudent = studentDao.findById(1);
        assertTrue(deleteCollegeStudent.isPresent(), "should return true");
        studentService.deleteStudent(1);
        deleteCollegeStudent = studentDao.findById(1);
        assertFalse(deleteCollegeStudent.isPresent(), "should return false");
    }
    @Sql("/insertData.sql")
    @Test
    public void getGradeBookService () {
        Iterable<CollegeStudent> collegeStudentIterable = studentService.getGradeBook();
        List<CollegeStudent> collegeStudents = new ArrayList<>();
        for (CollegeStudent collegeStudent : collegeStudentIterable) {
            collegeStudents.add(collegeStudent);
        }
        assertEquals(5, collegeStudents.size());
    }

    @Test
    public void createGradeService () {
        // create grade
        assertTrue(studentService.createGrade(80.50, 1, "math"));
        // get all grades with studentId
        Iterable<MathGrade> mathGrades = mathGradesDao.findGradeByStudentId(1);
        //verify grades
        assertTrue(mathGrades.iterator().hasNext(), "student has math grades");

    }

    @AfterEach
    public void setupAfterTransaction () {
        jdbc.execute("DELETE FROM student");
    }



}
