package L24_Test_Framework.framework.printer;



import L24_Test_Framework.framework.executions.Execution;

import java.util.List;

public interface Printer {
    void write(List<Execution> executions);
}
