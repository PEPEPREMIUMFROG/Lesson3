package L24_Test_Framework.framework.assertions;

public class AssertEqualResult<T> extends TestResult {
    private final T expectedResult;
    private final T actualResult;

    public AssertEqualResult(T expectedResult, T actualResult, boolean success) {
        super(success);
        this.expectedResult = expectedResult;
        this.actualResult = actualResult;
    }



    @Override
    public String toString() {
        return String.format("%-20s | %-20s","expected=" + expectedResult,
                "actual=" + actualResult);

    }
}
