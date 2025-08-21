package Lesson19.Task6;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Boyscout> scouts = Arrays.asList(
                new Boyscout("Иван", 13, Team.RED),
                new Boyscout("Петя", 12, Team.BLUE),
                new Boyscout("Коля", 14, Team.RED),
                new Boyscout("Дима", 16, Team.BLUE)
        );
        Camp camp = new Camp(scouts);
        camp.printTeams(camp.split());

    }
}
