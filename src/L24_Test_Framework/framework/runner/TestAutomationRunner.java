package L24_Test_Framework.framework.runner;

import L24_Test_Framework.framework.Marker.Test;
import L24_Test_Framework.framework.assertions.AssertException;
import L24_Test_Framework.framework.executions.Execution;
import L24_Test_Framework.framework.executions.ExecutionItem;
import L24_Test_Framework.framework.printer.Printer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TestAutomationRunner implements Runner {

    private final Printer printer;

    public TestAutomationRunner(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void run(List<Class<?>> testClasses) {
        List<Execution> executions = testClasses.stream().map(this::runTestClass).toList();
        printer.write(executions);
    }

    protected Execution runTestClass(Class<?> testClass){
        Object testClassInstance = getTestClassInstance(testClass);
        LocalDateTime startTime = LocalDateTime.now();
        List<ExecutionItem> executionItems = Arrays.stream(testClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Test.class))
                .map(method -> runTestMethod(method, testClassInstance))
                .toList();

       return new Execution(testClass, executionItems, startTime, LocalDateTime.now());
    }


    private Object getTestClassInstance(Class<?> testClass){
        try {
           return testClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            String error = "Test class must contains public constructor: " + testClass.getName();
            throw new RuntimeException(error);
        }
    }

    private ExecutionItem runTestMethod(Method method, Object testClassInstance) {
        try {
            method.invoke(testClassInstance);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof AssertException ae) {
                return new ExecutionItem(method, ae.getAssertResult());
            }
            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Error: test method " + method + "didnt throw AssertException");
    }
}
