package Lesson11.Task2.Obstacles;

import Lesson11.Task2.Participants.Participant;

public class Wall implements Obstacle {

    private int height;

    public Wall(int height) {
        this.height = height;
    }

    @Override
    public int overcome(Participant participant) {
        return participant.jump(height);
    }

    @Override
    public String getDescription() {
        return "стена";
    }

    @Override
    public String getMeasure() {
        return "см";
    }

    @Override
    public int getValue() {
        return height;
    }
}
