package L24_Test_Framework.framework.assertions;

public class AssertException extends Exception {

  private final TestResult assertResult;

  public AssertException(TestResult assertResult) {
    this.assertResult = assertResult;
  }

  public TestResult getAssertResult() {
    return assertResult;
  }
}
