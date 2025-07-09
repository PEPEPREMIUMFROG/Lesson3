package Lesson10;

public class Dog extends Animal{

    private static final int MAX_LENGTH_OF_RUN = 500;
    private static final int MAX_LENGTH_OF_SWIM = 10;
    private static int dogsCount = 0;

    public Dog(String name) {
       super(name);
       dogsCount++;
    }

    public static int getDogsCount() {
        return dogsCount;
    }

    @Override
    public void run(int distance) {
        if (distance > MAX_LENGTH_OF_RUN){
            System.out.println("Собаки не бегают так далеко!");
        }
        else {
            System.out.printf("%s пробежал %s метров \n", getName(), distance);
        }
    }

    @Override
    public void swim(int distance) {
        if (distance > MAX_LENGTH_OF_SWIM){
            System.out.println("Собаки не плавают так далеко!");
        }
        else {
            System.out.printf("%s проплыл %s метров \n",getName(), distance);
        }
    }
}
