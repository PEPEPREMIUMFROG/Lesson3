package L24_Test_Framework.framework.assertions;

import L24_Test_Framework.project.tests.RecursiveEqualsTest;

import java.util.Arrays;

public class AssertRecursiveEqualResult extends TestResult {
    private final Object expected;
    private final Object actual;

    public AssertRecursiveEqualResult(Object expected, Object actual, boolean success) {
        super(success);
        this.expected = expected;
        this.actual = actual;
    }
    @Override
    public String toString() {
        return String.format("%-10s | %-10s",
                "Expected: " + objectToString(expected),
                "Actual: " + objectToString(actual));
    }

    private String objectToString(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj.getClass().isArray()) {
            if (obj instanceof Object[]) {
                return Arrays.deepToString((Object[]) obj);
            } else if (obj instanceof byte[]) {
                return Arrays.toString((byte[]) obj);
            } else if (obj instanceof short[]) {
                return Arrays.toString((short[]) obj);
            } else if (obj instanceof int[]) {
                return Arrays.toString((int[]) obj);
            } else if (obj instanceof long[]) {
                return Arrays.toString((long[]) obj);
            } else if (obj instanceof char[]) {
                return Arrays.toString((char[]) obj);
            } else if (obj instanceof float[]) {
                return Arrays.toString((float[]) obj);
            } else if (obj instanceof double[]) {
                return Arrays.toString((double[]) obj);
            } else if (obj instanceof boolean[]) {
                return Arrays.toString((boolean[]) obj);
            }
        }
        return obj.toString();
    }
}