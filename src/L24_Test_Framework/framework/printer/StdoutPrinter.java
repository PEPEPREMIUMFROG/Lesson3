package L24_Test_Framework.framework.printer;

import L24_Test_Framework.framework.executions.Execution;
import L24_Test_Framework.framework.utils.ConsoleColors;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class StdoutPrinter implements Printer {

    private String template;

    public StdoutPrinter(String template) {
        this.template = template;
    }

    @Override
    public void write(List<Execution> executions) {
        for (var e : executions) {
            System.out.println("Class: " + e.getTestClass().getName()); //
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(template);
            System.out.println("Start time: " + dtf.format(e.getStartTime()));

            System.out.println("End time: " + dtf.format(e.getEndTime()));
            System.out.println("\n--------------------TESTS-----------------");
            e.getExecutionItems().forEach(item -> {
                String colorCode = item.getAssertResult().isSuccess() ? ConsoleColors.ANSI_GREEN : ConsoleColors.ANSI_RED;
                System.out.print(colorCode);
                System.out.printf("%s\n %s%n\n",item.getMethod().getName(), item.getAssertResult().toString());
            });
            System.out.print(ConsoleColors.ANSI_RESET);
            System.out.println("-------------------------------------------");
            System.out.println("\n\n");
        }
    }
}
