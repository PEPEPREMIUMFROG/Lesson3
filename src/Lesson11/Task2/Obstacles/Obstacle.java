package Lesson11.Task2.Obstacles;

import Lesson11.Task2.Participants.Participant;

public interface Obstacle {
    int overcome(Participant participant);
    String getDescription();
    String getMeasure();
    int getValue();
}
