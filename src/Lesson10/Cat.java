package Lesson10;

public class Cat extends Animal{
    private static final int MAX_LENGTH_OF_RUN = 200;
    private static int catsCount = 0;


    public static int getCatsCount() {
        return catsCount;
    }

    public Cat(String name) {
        super(name);
        catsCount++;
    }

    @Override
    public void run(int distance) {
        if (distance > MAX_LENGTH_OF_RUN){
            System.out.println("Коты не бегают так далеко!");
        }
        else {
            System.out.printf("%s пробежал %s метров \n",getName(), distance);
        }
    }

    @Override
    public void swim(int distance) {
        System.out.println("Коты не умеют плавать!");
    }
}
