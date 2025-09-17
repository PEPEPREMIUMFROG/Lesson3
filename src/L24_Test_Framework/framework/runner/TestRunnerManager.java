package L24_Test_Framework.framework.runner;

import L24_Test_Framework.framework.printer.Printer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunnerManager {
    private final List<RunnerAssignment> assignments;

    public TestRunnerManager() {
        this.assignments = new ArrayList<>();
    }

    public void assignTests(Printer printer, List<Class<?>> testClasses) {
        TestAutomationRunner runner = new TestAutomationRunner(printer);
        assignments.add(new RunnerAssignment(runner, testClasses));
    }

    public void assignTest(Printer printer, Class<?> testClass) {
        assignTests(printer, Arrays.asList(testClass));
    }

    public void run() {
        assignments.forEach(assignment -> {
            assignment.runner.run(assignment.testClasses);
        });
    }

    private static class RunnerAssignment {
        final TestAutomationRunner runner;
        final List<Class<?>> testClasses;

        RunnerAssignment(TestAutomationRunner runner, List<Class<?>> testClasses) {
            this.runner = runner;
            this.testClasses = testClasses;
        }
    }
}