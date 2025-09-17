package L24_Test_Framework.project.tests;


import L24_Test_Framework.framework.Marker.Test;
import L24_Test_Framework.framework.assertions.AssertException;
import L24_Test_Framework.framework.assertions.Assertions;
import L24_Test_Framework.project.function.Calculator;


public class CalculatorTest {

    @Test
    public void testSum() throws AssertException {
        Assertions.equals(5, Calculator.sum(2, 3));
    }

    @Test
    public void testSubtract() throws AssertException {
        Assertions.equals(4, Calculator.subtract(10, 2));
    }

    @Test
    public void testMultiply() throws AssertException {
        Assertions.equals(6L, Calculator.multiply(2, 3));
    }

    @Test
    public void testDivide() throws AssertException {
        Assertions.equals(2.0, Calculator.divide(6, 3));
    }
}