package com.luv2code.junitdemo;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
//@DisplayNameGeneration(DisplayNameGenerator.IndicativeSentences.class)
// or
//@DisplayNameGenerat
// ion(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DemoUtilsTest {
    DemoUtils demoUtils;

    @BeforeEach
    void setupBeforeEach () {
        demoUtils = new DemoUtils();
        System.out.println("@BeforeEach executes before the execution of each test method");
    }


    @Test
    @DisplayName("Equals and not Equals")
    @Order(1)
    void testEqualsAndNotEquals () {

        System.out.println("Runing test: testEqualsAndNotEquals");

        assertEquals(6, demoUtils.add(2,4), "2 + 4 must be 6");
        assertNotEquals(6, demoUtils.add(1,9), "1 + 9 must not equal 6");
    }

    @Test
    @DisplayName("Null and not Null")
    @Order(0)
    void testNullAndNotNull () {

        //System.out.println("Running test: testNullAndNotNull");

        String str1 = null;
        String str2 = "Hello";

        assertNull(demoUtils.checkNull(str1), "object should be null");
        assertNotNull(demoUtils.checkNull(str2), "object should not be null");
    }
    @Test
    @DisplayName("Same and not same")
    void testSameAndNotSame () {
        String str = "Amany";

        assertSame(demoUtils.getAcademy(),demoUtils.getAcademyDuplicate(), "objects should refer to the same object");
        assertNotSame(str, demoUtils.getAcademy(), "objects should not refer to the same object");
    }

    @Test
    @DisplayName("true or false")
    void testTrueFalse () {
        int gradeOne = 10;
        int gradeTwo = 5;

        assertTrue(demoUtils.isGreater(gradeOne,gradeTwo), "should return true");
        assertFalse(demoUtils.isGreater(gradeTwo,gradeOne), "should return false");
    }
    @DisplayName("Array Equals")
    @Test
    void testArrayEqurals () {
        String[] stringArray= {"A", "B", "C"};
        assertArrayEquals(stringArray, demoUtils.getFirstThreeLettersOfAlphabet(), " Arrays should be equal");
    }
    @DisplayName("Iterable equals")
    @Test
    void testIterableEquals () {
        List<String> theList = List.of("luv", "2", "code");
        assertIterableEquals(theList, demoUtils.getAcademyInList(), "expected list should be same as actual list");
    }
    @DisplayName("lines match")
    @Test
    void testLinesMatch () {
        List<String> theList = List.of("luv", "2", "code");
        assertLinesMatch(theList, demoUtils.getAcademyInList(), "lines should match");
    }
    
    @DisplayName("test throws and does not throw")
    @Test
    void testThrowAndDoesNotThrow () {
        int a = 5;
        assertThrows(Exception.class, ()->{demoUtils.throwException(-1);}, "should throw an exception");
        //assertDoesNotThrow(Exception.class, ()->{demoUtils.throwException(5);}, "should not throw an exception");
        assertDoesNotThrow(()->{demoUtils.throwException(5);}, "should not throw an exception");
    }

    @DisplayName("timeout")
    @Test
    void testTimeout () {
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {demoUtils.checkTimeout();},
                "method should execute in 3 seconds");
    }
    @DisplayName("multiply")
    @Test
    void testMultiply () {
        assertEquals(12, demoUtils.multiply(3,4),
                "3 * 4 must be 12");
    }






    /*
    @AfterEach
    void tearDownAfterEAch () {
        System.out.println("Running after each");
        System.out.println();
    }

    @BeforeAll
    static void setupBeforeAll () {
        System.out.println("@BeforeAll executes once before all test methods execution in the class");
    }

    @AfterAll
    static void tearDownAfterAll () {
        System.out.println("@afterall executes once after all test methods execution in the class");
    }
     */


}
