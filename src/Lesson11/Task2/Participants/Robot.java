package Lesson11.Task2.Participants;

public class Robot implements Participant {
    private String name;
    private int maxRunLength;
    private int maxJumpHeight;

    public Robot(String name, int maxRunLength, int maxJumpHeight) {
        this.name = name;
        this.maxRunLength = maxRunLength;
        this.maxJumpHeight = maxJumpHeight;
    }

    @Override
    public int run(int length) {
        return Math.min(length, maxRunLength);
    }

    @Override
    public int jump(int height) {
        return Math.min(height, maxJumpHeight);
    }

    @Override
    public String getName() {
        return name;
    }
}
