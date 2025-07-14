package Lesson11.Task2;

import Lesson11.Task2.Obstacles.Obstacle;
import Lesson11.Task2.Obstacles.Threadmill;
import Lesson11.Task2.Obstacles.Wall;
import Lesson11.Task2.Participants.Cat;
import Lesson11.Task2.Participants.Human;
import Lesson11.Task2.Participants.Participant;
import Lesson11.Task2.Participants.Robot;

public class Main {
    public static void main(String[] args) {
        Participant[] participants = {
                new Human("Олег", 100, 200),
                new Human("Иван", 120, 175),
                new Cat("Барсик", 60, 120),
                new Cat("Рыжик", 115, 180),
                new Robot("Электроник", 500, 500)
        };
        Obstacle[] obstacles = {
                new Threadmill(60),
                new Wall(170),
                new Threadmill(115),
                new Wall(205)

        };
        Competition competition = new Competition();
        competition.start(participants, obstacles);
    }
}
