package L24_Test_Framework;

import L24_Test_Framework.framework.printer.FilePrinter.FilePrinter;
import L24_Test_Framework.framework.printer.Printer;
import L24_Test_Framework.framework.printer.StdoutPrinter;
import L24_Test_Framework.framework.runner.ExtendedTestAutomationRunner;
import L24_Test_Framework.framework.runner.TestAutomationRunner;
import L24_Test_Framework.framework.runner.TestRunnerManager;
import L24_Test_Framework.project.tests.ArrContainsTest;
import L24_Test_Framework.project.tests.CalculatorLineTest;
import L24_Test_Framework.project.tests.CalculatorTest;
import L24_Test_Framework.project.tests.RecursiveEqualsTest;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) {
        Printer printer = new StdoutPrinter("yyyy-MM-dd HH:mm:ss ");
        Printer printer1 = new FilePrinter();
        Printer printer2 = new FilePrinter("yyyy-MM-dd", "calc_tets",
                "src/L24_Test_Framework/project/tests/logS/");
        CountDownLatch cdl = new CountDownLatch(3);

        List<Class<?>> allTests = List.of(CalculatorTest.class, CalculatorLineTest.class,
                ArrContainsTest.class, RecursiveEqualsTest.class);

        TestRunnerManager trm = new TestRunnerManager();
        TestAutomationRunner tar = new TestAutomationRunner(printer);
        ExtendedTestAutomationRunner etar = new ExtendedTestAutomationRunner(Set.of(printer, printer1, printer2));

        try {
            System.out.println("trm starts");
            trm.assignTest(printer, CalculatorTest.class);
            trm.assignTest(printer1, CalculatorTest.class);
            trm.assignTests(printer2, allTests);
            trm.run();
            System.out.println("trm completed tasks");
            Thread.sleep(3000);
            cdl.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            System.out.println("Tar starts");
            tar.run(List.of(CalculatorTest.class));
            cdl.countDown();
            System.out.println("Tar completed tasks");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("etar start");
        etar.run(allTests);
        cdl.countDown();
        System.out.println("etar completed tasks");
        try {
            System.out.println("All tests have been provided");
            cdl.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
