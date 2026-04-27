package com.luv2code.tdd;

public class FizzBuzz {
    // If number divisible by 3, print Fizz
    // If number divisible by 5, print Buzz
    // If number divisible by 3 and 5, print FizzBuzz
    // If number not divisible by 3 or 5, print the number

    public static String compute(int pNumber) {
        StringBuilder lResult = new StringBuilder();
        if (pNumber % 3 == 0) {
            lResult.append("Fizz");
        }
        if (pNumber % 5 == 0) {
            lResult.append("Buzz");
        }
        if (lResult.isEmpty()) {
            lResult.append(pNumber);
        }
        return lResult.toString();
    }

    public static String compute1(int pNumber) {

        if (pNumber % 3 == 0 && pNumber % 5 == 0) {
            return "FizzBuzz";
        }
        else if (pNumber % 3 == 0) {
            return "Fizz";
        }
        else if (pNumber % 5 == 0) {
            return "Buzz";
        }
        return Integer.toString(pNumber);
    }

}
