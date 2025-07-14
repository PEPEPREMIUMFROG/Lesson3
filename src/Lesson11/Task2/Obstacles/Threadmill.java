package Lesson11.Task2.Obstacles;

import Lesson11.Task2.Participants.Participant;

public class Threadmill implements Obstacle {

    private int length;

    public Threadmill(int length) {
        this.length = length;
    }

    @Override
    public int overcome(Participant participant) {
        return participant.run(length);
    }

    @Override
    public String getDescription() {
        return "беговая дорожка";
    }

    @Override
    public String getMeasure() {
        return "м";
    }

    @Override
    public int getValue() {
        return length;
    }
}
