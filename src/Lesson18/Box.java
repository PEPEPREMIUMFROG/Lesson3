package Lesson18;

import java.util.ArrayList;
import java.util.List;

public class Box<T extends Fruit> {
    private final List<T> fruits;
    private final Class<T> fruitType;

    public Box(Class<T> fruitType) {
        this.fruits = new ArrayList<>();
        this.fruitType = fruitType;
    }

    public void add(T fruit){
        if (fruitType.isInstance(fruit)){
            fruits.add(fruit);
        }
        else {
            throw new IllegalArgumentException("wrong fruit type");
        }
    }

    public float getWeight(){
        float totalWeight = 0.0f;
        for (T fruit : fruits){
            totalWeight += fruit.getWeight();
        }
        return totalWeight;
    }

    public boolean compare(Box<? extends  Fruit> otherBox ){
        return Math.abs(this.getWeight() - otherBox.getWeight()) == 0;
    }

    public void transfer(Box<T> otherBox){
        if (otherBox.fruitType != this.fruitType){
            throw new IllegalArgumentException("different fruit type in a boxes");
        }
        for (T fruit : this.fruits){
            otherBox.add(fruit);
        }
        this.fruits.clear();
    }

    @Override
    public String toString() {
        return "Box{" +
                "fruits=" + fruits +
                ", fruitType=" + fruitType.getSimpleName() +
                '}';
    }
}
