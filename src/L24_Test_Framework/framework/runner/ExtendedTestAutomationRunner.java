package L24_Test_Framework.framework.runner;

import L24_Test_Framework.framework.executions.Execution;
import L24_Test_Framework.framework.printer.Printer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ExtendedTestAutomationRunner extends TestAutomationRunner {

    private final Set<Printer> printers;

    public ExtendedTestAutomationRunner(Set<Printer> printers) {
        super(getFirstPrinter(printers));
        this.printers = new HashSet<>(printers);
    }

    public void addPrinter(Printer printer) {
        if (printer != null) {
            this.printers.add(printer);
        }
    }

    public void removePrinter(Printer printer) {
        this.printers.remove(printer);
    }

    public Set<Printer> getPrinters() {
        return new HashSet<>(printers);
    }

    @Override
    public void run(List<Class<?>> testClasses) {
        List<Execution> executions = testClasses.stream()
                .map(this::runTestClass)
                .toList();
        for (Printer printer : printers) {
            printer.write(executions);
        }
    }

    private static Printer getFirstPrinter(Set<Printer> printers) {
        if (printers == null || printers.isEmpty()) {
            throw new IllegalArgumentException("At least one printer must be provided");
        }
        return printers.iterator().next();
    }
}