package Lesson15;

public class Main {
    public static void main(String[] args) {
        String[][] data = {{"user1", "09:15"}, {"user2", "10:00"}, {"user1", "09:45"},
                {"user3", "11:30"}, {"user2", "10:20"}, {"user4", "14:10"}, {"user1", "09:55"},
                {"user2", "10:45"}, {"user4", "14:25"}, {"user3", "11:50"}, {"user5", "15:00"},
                {"user1", "16:30"}, {"user3", "11:59"}};
        AttandanceLogger logger = new AttandanceLogger();
        logger.addVisits(data);
        logger.printMostPopularHour();
        logger.printUserFrequency();


    }
}
