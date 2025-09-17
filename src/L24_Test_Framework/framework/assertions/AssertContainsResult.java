package L24_Test_Framework.framework.assertions;

public class AssertContainsResult extends TestResult {
    private final String input;
    private final String expected;

    public AssertContainsResult(boolean success, String input, String expected) {
        super(success);
        this.input = input;
        this.expected = expected;
    }

    @Override
    public String toString() {
        return String.format("%-20s | %-20s","Input: '" + input+"'",
                "line to contains: '" + expected+"'");

    }
}
