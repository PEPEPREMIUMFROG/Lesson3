package Lesson18;

public class Task2 {
    public static void main(String[] args) {
        Box<Apple> firstAppleBox = new Box<>(Apple.class);
        Box<Apple> secondAppleBox = new Box<>(Apple.class);
        Box<Orange> orangeBox = new Box<>(Orange.class);

        firstAppleBox.add(new Apple());
        firstAppleBox.add(new Apple());
        firstAppleBox.add(new Apple());
        orangeBox.add(new Orange());
        orangeBox.add(new Orange());

        System.out.println("First apple box weight: " + firstAppleBox.getWeight());
        System.out.println("Orange box weight: " + orangeBox.getWeight());
        System.out.println("First apple box and Orange box have equal weight: " +
                firstAppleBox.compare(orangeBox));

        secondAppleBox.add(new Apple());
        secondAppleBox.add(new Apple());

        System.out.println("Second apple box weight: " + secondAppleBox.getWeight());
        System.out.println("First apple box and Second apple box have equal weight: " +
                firstAppleBox.compare(secondAppleBox));

        System.out.println();
        System.out.println("Before transfer First apple box: \n" + firstAppleBox);
        System.out.println("Before transfer Second apple box: \n" + secondAppleBox);

        firstAppleBox.transfer(secondAppleBox);

        System.out.println("After transfer First apple box : \n" + firstAppleBox);
        System.out.println("After transfer Second apple box: \n" + secondAppleBox);
        System.out.println("Second apple box weight after transfer: " + secondAppleBox.getWeight());
        System.out.println("Orange box: \n" + orangeBox);


    }
}
