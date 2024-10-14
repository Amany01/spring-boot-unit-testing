package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
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
import java.util.Collection;
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
    @Autowired
    private ScienceGradesDao scienceGradesDao;
    @Autowired
    private HistoryGradesDao historyGradesDao;
    @BeforeEach
    public void setupDatabase () {
        jdbc.execute("INSERT INTO student(id, firstname, lastname, email_address)" +
                "VALUES (1, 'Eric', 'Roby', 'eric@roby.com')");
        jdbc.execute("INSERT INTO math_grade(id,student_id,grade) VALUES (1,1,100.00)");
        jdbc.execute("INSERT INTO science_grade(id,student_id,grade) VALUES (1,1,100.00)");
        jdbc.execute("INSERT INTO history_grade(id,student_id,grade) VALUES (1,1,100.00)");
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

        Optional<MathGrade> deleteMathGrade = mathGradesDao.findById(1);
        Optional<ScienceGrade> deleteScienceGrade = scienceGradesDao.findById(1);
        Optional<HistoryGrade> deleteHistoryGrade = historyGradesDao.findById(1);

        assertTrue(deleteCollegeStudent.isPresent(), "should return true");
        assertTrue(deleteMathGrade.isPresent());
        assertTrue(deleteScienceGrade.isPresent());
        assertTrue(deleteHistoryGrade.isPresent());

        studentService.deleteStudent(1);

        deleteCollegeStudent = studentDao.findById(1);
        deleteMathGrade = mathGradesDao.findById(1);
        deleteScienceGrade = scienceGradesDao.findById(1);
        deleteHistoryGrade = historyGradesDao.findById(1);

        assertFalse(deleteCollegeStudent.isPresent(), "should return false");
        assertFalse(deleteMathGrade .isPresent());
        assertFalse(deleteScienceGrade .isPresent());
        assertFalse(deleteHistoryGrade .isPresent());
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
        assertTrue(studentService.createGrade(80.50, 1, "science"));
        assertTrue(studentService.createGrade(80.50, 1, "history"));
        // get all grades with studentId
        Iterable<MathGrade> mathGrades = mathGradesDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradesDao.findGradeByStudentId(1);
        //verify grades
        assertTrue(((Collection<MathGrade>) mathGrades).size() == 2, "student has math grades");
        assertTrue(((Collection<ScienceGrade>) scienceGrades).size() == 2, "student has science grades");
        assertTrue(((Collection<HistoryGrade>) historyGrades).size() == 2, "student has history grades");

    }

    @Test
    public void createGradeServiceReturnFalse () {
        assertFalse(studentService.createGrade(105.0, 1, "math"));
        assertFalse(studentService.createGrade(-5.0, 1, "math"));
        assertFalse(studentService.createGrade(80.0, 2, "math"));
        assertFalse(studentService.createGrade(80.0, 1, "literature"));
    }

    @Test
    public void deleteGradeService () {
        //           studentId
        //                                                  gradeId
        assertEquals(1, studentService.deleteGrade(1, "math"),
                "return student id after deletion");
        assertEquals(1, studentService.deleteGrade(1, "science"),
                "return student id after deletion");
        assertEquals(1, studentService.deleteGrade(1, "history"),
                "return student id after deletion");
    }

    @Test
    public void deleteGradeServiceReturnStudentIdOfZero () {
        //invalid grade id
        assertEquals(0, studentService.deleteGrade(0, "science"),
                "no student should have 0 id");
        // invalid subject
        assertEquals(0, studentService.deleteGrade(1, "literature"),
                "no student should have a literature class");

    }

    @AfterEach
    public void setupAfterTransaction () {

        jdbc.execute("DELETE FROM student");
        jdbc.execute("DELETE FROM math_grade");
        jdbc.execute("DELETE FROM science_grade");
        jdbc.execute("DELETE FROM history_grade");
    }



}