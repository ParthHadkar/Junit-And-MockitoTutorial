package com.luv2code.junitdemo;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//@DisplayNameGeneration(DisplayNameGenerator.IndicativeSentences.class)
//@TestMethodOrder(MethodOrderer.DisplayName.class)
//@TestMethodOrder(MethodOrderer.MethodName.class)
//@TestMethodOrder(MethodOrderer.Random.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DemoUtilsTest {

    DemoUtils demoUtils;

    @BeforeEach
    void setupBeforeEach() {
        System.out.println("@BeforeEach executes before execution of each test method");
        demoUtils = new DemoUtils();
    }

    @AfterEach
    void tearDownAfterEach() {
        System.out.println("@AfterEach executes after execution of each test method \n");
    }

    @BeforeAll
    static void setupBeforeEachClass() {
        System.out.println("@BeforeAll executes only once before execution of all test methods in the class");
    }

    @AfterAll
    static void tearDownAfterAll() {
        System.out.println("@AfterAll executes only once after execution of all test methods  in the class");
    }

    @Test
    @DisplayName("Multiply")
    void testMultiply() {
        assertEquals(12, demoUtils.multiply(4, 3), "4 * 3 must be 12");
    }

    @Test
    @DisplayName("Equals and Not Equals")
    @Order(3)
    void test_Equals_And_Not_Equals() {

        System.out.println("Running test: testEqualsAndNotEquals");
        // initialise the class

        // excute and assert

        assertEquals(6, demoUtils.add(2, 4), "2 + 4 must be 6");
        assertNotEquals(6, demoUtils.add(1, 9), "1 + 9 must not be 6");
    }

    @Test
    @DisplayName("Null and Not Null")
    @Order(1)
    void testNullAndNotNull() {

        System.out.println("Running test: testNullAndNotNull");

        DemoUtils lDemoUtils = new DemoUtils();

        String lStr1 = null;
        String lStr2 = "luv2code";

        assertNull(lDemoUtils.checkNull(lStr1), "Object should be null");
        assertNotNull(lDemoUtils.checkNull(lStr2), "Object should not be null");
    }

    @Test
    @DisplayName("Same and Not Same")
    void testSameAndNotSame() {
        String lStr = "luv2Code";

        assertSame(demoUtils.getAcademy(), demoUtils.getAcademyDuplicate(), "Object should refer to same object");
        assertNotSame(lStr, demoUtils.getAcademy(), "Object should not refer to same object");
    }

    @Test
    @DisplayName("True and False")
    @Order(0)
    void testTrueFalse() {
        int lGradeOne = 10;
        int lGradeTwo = 5;
        assertTrue(demoUtils.isGreater(lGradeOne, lGradeTwo), "This should return true");
        assertFalse(demoUtils.isGreater(lGradeTwo, lGradeOne), "This should return false");
    }

    @Test
    @DisplayName("Array Equals")
    @Order(-7)
    void testArrayEquals() {
        String[] lStringArr = {"A", "B", "C"};
        assertArrayEquals(lStringArr, demoUtils.getFirstThreeLettersOfAlphabet(), "Arrays should be the same");
    }

    @Test
    @DisplayName("Iterable Equals")
    void testIterableEquals() {
        List<String> lList = List.of("luv", "2", "code");
        assertIterableEquals(lList, demoUtils.getAcademyInList(), "Expected list should be same as actual list");
    }

    @Test
    @DisplayName("Lines Match")
    @Order(30)
    void testLinesMatch() {
        List<String> lList = List.of("luv", "2", "code");
        assertLinesMatch(lList, demoUtils.getAcademyInList(), "Lines should match");
    }

    @Test
    @DisplayName("Throws and Does Not Throw")
    void testThrowsAndDoesNotThrow() {
        assertThrows(Exception.class, () -> {demoUtils.throwException(-1);}, "Should throw exception");
        assertDoesNotThrow(() -> {demoUtils.throwException(5);}, "Should not throw exception");
    }

    @Test
    @DisplayName("Timeout")
    @Order(50)
    void testTimeout() {
        assertTimeoutPreemptively( Duration.ofSeconds(3),() -> {demoUtils.checkTimeout();}, "Method should execute in 3 seconds");
    }
}
