package Lesson10;

public abstract class Animal {

    private String name;
    protected static int totalAnimalsCount = 0;

    public Animal(String name) {
        setName(name);
        totalAnimalsCount++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void run(int distance);

    public abstract void swim(int distance);

    public static int getTotalAnimalsCount() {
        return totalAnimalsCount;
    }
}
