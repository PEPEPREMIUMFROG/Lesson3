package L24_Test_Framework.framework.executions;

import java.time.LocalDateTime;
import java.util.List;

public class Execution {
    private Class<? > testClass;
    private List<ExecutionItem> executionItems;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Execution(Class<?> testClass, List<ExecutionItem> executionItems, LocalDateTime startTime, LocalDateTime endTime) {
        this.testClass = testClass;
        this.executionItems = executionItems;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Execution{" +
                "testClass=" + testClass +
                ", executionItems=" + executionItems +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public List<ExecutionItem> getExecutionItems() {
        return executionItems;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
