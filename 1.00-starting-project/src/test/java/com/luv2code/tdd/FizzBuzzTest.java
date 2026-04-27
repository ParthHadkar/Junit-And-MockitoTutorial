package com.luv2code.tdd;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FizzBuzzTest {

    // If number divisible by 3, print Fizz
    @Test
    @DisplayName("Divisible By Three")
    @Order(1)
    void testForDivisibleByThree() {
        //fail("fail");
        String lExcepted = "Fizz";
        assertEquals(lExcepted, FizzBuzz.compute(3), "Should return Fizz");
    }

    // If number divisible by 5, print Buzz
    @Test
    @DisplayName("Divisible By Five")
    @Order(2)
    void testForDivisibleByFive() {
        String lExcepted = "Buzz";
        assertEquals(lExcepted, FizzBuzz.compute(5), "Should return Buzz");
    }

    // If number divisible by 3 and 5, print FizzBuzz
    @Test
    @DisplayName("Divisible By Three And Five")
    @Order(3)
    void testForDivisibleByThreeAndFive() {
        String lExcepted = "FizzBuzz";
        assertEquals(lExcepted, FizzBuzz.compute(15), "Should return FizzBuzz");
    }

    // If number not divisible by 3 or 5, print the number
    @Test
    @DisplayName("Not Divisible By Three Or Five")
    @Order(4)
    void testForNotDivisibleByThreeOrFive() {
        String lExcepted = "2";
        assertEquals(lExcepted, FizzBuzz.compute(2), "Should return 2");
    }


    @ParameterizedTest
    @CsvSource({
            "1,1",
            "2,2",
            "3,Fizz",
            "4,4",
            "5,Buzz",
            "6,Fizz",
            "7,7",

    })
    @DisplayName("Testing with csv data")
    @Order(5)
    void testCsvData(int pValue, String pExcepted) {
        assertEquals(pExcepted, FizzBuzz.compute(pValue), "Should return "+pExcepted);
    }

    @ParameterizedTest(name = "value={0}, excepted={1}")
    @CsvFileSource(resources = "/small-test-data.csv")
    @DisplayName("Testing with small data file")
    @Order(6)
    void testFileData(int pValue, String pExcepted) {
        assertEquals(pExcepted, FizzBuzz.compute(pValue), "Should return "+pExcepted);
    }

    @ParameterizedTest(name = "value={0}, excepted={1}")
    @CsvFileSource(resources = "/medium-test-data.csv")
    @DisplayName("Testing with medium data file")
    @Order(7)
    void testMediumFileData(int pValue, String pExcepted) {
        assertEquals(pExcepted, FizzBuzz.compute(pValue), "Should return "+pExcepted);
    }

    @ParameterizedTest(name = "value={0}, excepted={1}")
    @CsvFileSource(resources = "/large-test-data.csv")
    @DisplayName("Testing with large data file")
    @Order(8)
    void testLargeFileData(int pValue, String pExcepted) {
        assertEquals(pExcepted, FizzBuzz.compute(pValue), "Should return "+pExcepted);
    }

}
