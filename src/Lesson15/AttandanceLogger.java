package Lesson15;

import java.util.*;

public class AttandanceLogger {

    private List<Visit> visits = new ArrayList<>();
    private Map<String, Integer> visitCount = new HashMap<>();
    private Map<String, Integer> hourPopularity = new HashMap<>();

    public void addVisit(String userId, String timestamp){
        Visit visit = new Visit(userId, timestamp);
        visits.add(visit);
        visitCount.put(userId, visitCount.getOrDefault(userId,0 ) +1);
        String hour = timestamp.split(":")[0];
        hourPopularity.put(hour, hourPopularity.getOrDefault(hour, 0) + 1);
    }

    public void addVisits(String[][]data){
        for (String[] visit : data){
            addVisit(visit[0],visit[1]);
        }
    }

    public Map<String, Integer> getFrequencyVisits() {
        return visitCount;
    }

    public void printUserFrequency() {
        System.out.println("Частота посещений:");
        for (Map.Entry<String, Integer> entry : getFrequencyVisits().entrySet()) {
            String user = entry.getKey();
            Integer count = entry.getValue();
            System.out.println(user + ": " + count);
        }
    }

    public String getMostPopularHour() {
        int maxVisits = 0;
        String popularHour = null;
        for (Map.Entry<String, Integer> entry : hourPopularity.entrySet()) {
            if (entry.getValue() > maxVisits
                    || entry.getValue() == maxVisits
                    && (popularHour == null || entry.getKey().compareTo(popularHour) < 0)) {
                maxVisits = entry.getValue();
                popularHour = entry.getKey();
            }
        }
        return popularHour != null ? popularHour + ":00 (" + maxVisits + " посещений)" : "Нет данных";
    }

    public void printMostPopularHour() {
        System.out.println("\nСамый популярный час: " + getMostPopularHour());
    }

    private static class Visit{
        final String userId;
        final String timestamp;

        private Visit(String userId, String timestamp) {
            this.userId = userId;
            this.timestamp = timestamp;
        }
    }
}



