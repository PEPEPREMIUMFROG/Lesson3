package Lesson19.Task6;

import java.util.*;
import java.util.stream.Collectors;

public class Camp {
    private final List<Boyscout> boyscouts;

    public Camp(List<Boyscout> boyscouts) {
        this.boyscouts = boyscouts;
    }

    public Map<Team, List<Boyscout>> split() {
        return boyscouts.stream()
                .collect(Collectors.groupingBy(Boyscout::getTeam, Collectors.mapping(b -> b,
                        Collectors.collectingAndThen(Collectors.toList(),list ->
                        {list.sort(Comparator.comparingInt(Boyscout::getAge)
                                .reversed());
            return list;
        }))));
    }


    public void printTeams(Map<Team, List<Boyscout>> teams) {
        for (Map.Entry<Team, List<Boyscout>> entry : teams.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
