package L24_Test_Framework.project.tests;


import L24_Test_Framework.framework.Marker.Test;
import L24_Test_Framework.framework.assertions.AssertException;
import L24_Test_Framework.framework.assertions.Assertions;
import L24_Test_Framework.project.function.Calculator;

public class CalculatorLineTest {

    @Test
    public void testSumLine() throws AssertException {
        String result = Calculator.sumLine(10, 10);
        Assertions.contains(result, "10+10=2");
    }

    @Test
    public void testSubtractLINE() throws AssertException {
        String result = Calculator.subtractLine(10, 10);
        Assertions.contains(result, "10-10=!!");
    }

    @Test
    public void testMultiplyLine() throws AssertException {
        String result = Calculator.multiplyLine(10, 10);
        Assertions.contains(result, "10*10=100");
    }

}
