package L23_Reflection_Annotation;

import L23_Reflection_Annotation.Annotations.*;

public class CalculatorTest {
    private Calculator calculator;
    private int testCount = 0;

    @BeforeSuite
    public void globalSetUp() {
        System.out.println("___ TESTING STARTED");
        calculator = new Calculator();
        System.out.println("Calculator initialized");
    }

    @BeforeEach
    public void setUp() {
        testCount++;
        System.out.println("Setting up test #" + testCount);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Completed test #" + testCount);
    }

    @AfterSuite
    public void globalTeardown() {
        System.out.println("___ TESTING COMPLETED WITH " + testCount + " TESTS");
    }

    @Test(order = 1, description = "Testing sum of positive numbers")
    public void testAddPositiveNumbers() {
        int result = calculator.add(15, 25);
        if (result == 40) {
            System.out.println("testAddPositiveNumbers - PASSED");
        } else {
            System.out.println("testAddPositiveNumbers - FAILED: Expected 40, Actual " + result);
        }
    }

    @Test(order = 2, description = "Testing sum of negative numbers")
    public void testAddNegativeNumbers() {
        int result = calculator.add(-10, -5);
        if (result == -15) {
            System.out.println("testAddNegativeNumbers - PASSED");
        } else {
            System.out.println("testAddNegativeNumbers - FAILED: Expected -15, Actual " + result);
        }
    }

    @Test(order = 0, description = "Testing summation of zero with number")
    public void testAddZeroAndNumber() {
        int result1 = calculator.add(0, 42);
        int result2 = calculator.add(42, 0);
        if (result1 == 42 && result2 == 42) {
            System.out.println("testAddZeroAndNumber - PASSED");
        } else {
            System.out.println("testAddZeroAndNumber - FAILED: Expected 42, Actual " + result1 + " and " + result2);
        }
    }

    @Test(order = 3, description = "Adding positive and negative number")
    public void testAddNegativeToPositive() {
        int result = calculator.add(10, -3);
        if (result == 7) {
            System.out.println("testAddNegativeToPositive - PASSED");
        } else {
            System.out.println("testAddNegativeToPositive - FAILED: Expected 7, Actual " + result);
        }
    }

    @Test(order = 4, description = "Adding maximum and minimum int values")
    public void testAddMinAndMaxInt() {
        int result = calculator.add(Integer.MAX_VALUE, Integer.MIN_VALUE);
        if (result == -1) {
            System.out.println("testAddMinAndMaxInt - PASSED");
        } else {
            System.out.println("testAddMinAndMaxInt - FAILED: Expected -1, Actual " + result);
        }
    }

    @Test( description = "Subtracting positive number from another positive", enabled = false)
    public void testSubtractPositiveFromPositive() {
        int result = calculator.subtract(20, 8);
        if (result == 12) {
            System.out.println("testSubtractPositiveFromPositive - PASSED");
        } else {
            System.out.println("testSubtractPositiveFromPositive - FAILED: Expected 12, Actual " + result);
        }
    }

    @Test(order = 5, description = "Subtracting negative number from positive")
    public void testSubtractNegativeFromPositive() {
        int result = calculator.subtract(15, -5);
        if (result == 20) {
            System.out.println("testSubtractNegativeFromPositive - PASSED");
        } else {
            System.out.println("testSubtractNegativeFromPositive - FAILED: Expected 20, Actual " + result);
        }
    }

    @Test(description = "Subtracting positive number from negative", enabled = false)
    public void testSubtractPositiveFromNegative() {
        int result = calculator.subtract(-10, 5);
        if (result == -15) {
            System.out.println("testSubtractPositiveFromNegative - PASSED");
        } else {
            System.out.println("testSubtractPositiveFromNegative - FAILED: Expected -15, Actual " + result);
        }
    }

    @Test(order = 6, description = "Subtracting equal values")
    public void testSubtractEqualValues() {
        int result = calculator.subtract(42, 42);
        if (result == 0) {
            System.out.println("testSubtractEqualValues - PASSED");
        } else {
            System.out.println("testSubtractEqualValues - FAILED: Expected 0, Actual " + result);
        }
    }

    @Test(order =7, description = "Multiplying two positive numbers")
    public void testMultiplyPositiveByPositive() {
        int result = calculator.multiply(6, 7);
        if (result == 42) {
            System.out.println("testMultiplyPositiveByPositive - PASSED");
        } else {
            System.out.println("testMultiplyPositiveByPositive - FAILED: Expected 42, Actual " + result);
        }
    }

    @Test( description = "Multiplying two negative numbers")
    public void testMultiplyNegativeByNegative() {
        int result = calculator.multiply(-4, -8);
        if (result == 32) {
            System.out.println("testMultiplyNegativeByNegative - PASSED");
        } else {
            System.out.println("testMultiplyNegativeByNegative - FAILED: Expected 32, Actual " + result);
        }
    }

    @Test(order = 10, description = "Multiplying any number by zero")
    public void testMultiplyByZero() {
        int result1 = calculator.multiply(0, 100);
        int result2 = calculator.multiply(-50, 0);
        if (result1 == 0 && result2 == 0) {
            System.out.println("testMultiplyByZero - PASSED");
        } else {
            System.out.println("testMultiplyByZero - FAILED: Expected 0, Actual " + result1 + " and " + result2);
        }
    }
}