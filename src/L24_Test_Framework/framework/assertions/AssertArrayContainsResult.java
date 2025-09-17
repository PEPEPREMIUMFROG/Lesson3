package L24_Test_Framework.framework.assertions;

import java.util.Arrays;

public class AssertArrayContainsResult extends TestResult {
    private final Object[] currentArray;
    private final Object[] expectedSubArray;
    private final String errorMessage;

    public AssertArrayContainsResult(boolean success, Object[] currentArray, Object[] expectedSubArray, String errorMessage) {
        super(success);
        this.currentArray = currentArray;
        this.expectedSubArray = expectedSubArray;
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        if (errorMessage != null && !errorMessage.isEmpty()) {
            return String.format("Error: %s | Current: %s | Expected subarray: %s",
                    errorMessage,
                    currentArray == null ? "null" : Arrays.toString(currentArray),
                    expectedSubArray == null ? "null" : Arrays.toString(expectedSubArray));
        }

        return String.format("%-30s | %-30s",
                "Current: " + (currentArray == null ? "null" : Arrays.toString(currentArray)),
                "Expected subarray: " + (expectedSubArray == null ? "null" : Arrays.toString(expectedSubArray)));
    }
}
