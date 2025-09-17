package L24_Test_Framework.framework.executions;


import L24_Test_Framework.framework.assertions.TestResult;

import java.lang.reflect.Method;

public class ExecutionItem  {

    private Method method;
    private TestResult assertResult;

    public ExecutionItem(Method method, TestResult assertResult) {
        this.method = method;
        this.assertResult = assertResult;
    }


    public Method getMethod() {
        return method;
    }

    public TestResult getAssertResult() {
        return assertResult;
    }

    @Override
    public String toString() {
        return "ExecutionItem{" +
                "method=" + method +
                ", assertResult=" + assertResult +
                '}';
    }

}
