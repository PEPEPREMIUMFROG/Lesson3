package Lesson18;

public class Orange extends Fruit{
    public Orange() {
        this.weight = 1.5f;
    }

    @Override
    public String toString() {
        return "Orange{" +
                "weight=" + weight +
                '}';
    }
}
