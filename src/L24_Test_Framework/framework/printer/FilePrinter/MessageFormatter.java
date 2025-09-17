package L24_Test_Framework.framework.printer.FilePrinter;

import L24_Test_Framework.framework.executions.Execution;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class MessageFormatter {

    private static final String SUCCESS_MARKER = "[SUCCESS]";
    private static final String FAILURE_MARKER = "[FAIL]";
    private static final String SECTION_TITLE = "TESTS";
    private static final int SECTION_LINE_TOTAL_WIDTH = 100;
    private static final String RESULT_LINE_SEPARATOR = "-".repeat(SECTION_LINE_TOTAL_WIDTH);

    public static String formatExecutions(List<Execution> executions, String template) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(template);
        String testsSectionHeaderLine = createCenteredHeaderLine(SECTION_TITLE, SECTION_LINE_TOTAL_WIDTH);
        StringBuilder fullMessage = new StringBuilder();
        for (var e : executions) {
            fullMessage.append(buildMessageForExecution(e, dtf, testsSectionHeaderLine));
        }
        return fullMessage.toString();
    }

    private static String buildMessageForExecution(Execution execution, DateTimeFormatter dtf, String sectionHeaderLine) {
        StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append("Class: ").append(execution.getTestClass().getName()).append("\n");
        messageBuilder.append("Start time: ").append(dtf.format(execution.getStartTime())).append("\n");
        messageBuilder.append("End time: ").append(dtf.format(execution.getEndTime())).append("\n");
        messageBuilder.append("\n").append(sectionHeaderLine).append("\n");
        for (var item : execution.getExecutionItems()) {
            String marker = item.getAssertResult().isSuccess() ? SUCCESS_MARKER : FAILURE_MARKER;
            messageBuilder.append(String.format("%s %s\n | %s%n\n", marker, item.getMethod().getName(),
                    item.getAssertResult().toString()));
        }
        messageBuilder.append(RESULT_LINE_SEPARATOR).append("\n");
        messageBuilder.append("\n\n");
        return messageBuilder.toString();
    }

    private static String createCenteredHeaderLine(String title, int totalWidth) {
        if (title == null) title = "";
        int titleLength = title.length();
        if (titleLength >= totalWidth) {
            return title.substring(0, Math.min(titleLength, totalWidth));
        }
        int dashesTotal = totalWidth - titleLength;
        int dashesBefore = dashesTotal / 2;
        int dashesAfter = dashesTotal - dashesBefore;
        return "-".repeat(dashesBefore) + title + "-".repeat(dashesAfter);
    }
}