package Lesson10;

public class Main {
    public static void main(String[] args) {

        Animal ginger = new Cat("Рэжик");
        ginger.run(150);
        ginger.swim(1);
        ginger.run(250);

        Animal caesar = new Dog("Цезарь");
        caesar.run(550);
        caesar.run(500);
        caesar.swim(4);
        caesar.swim(40);

        Animal bim = new Dog("Бим");
        bim.run(222);

        System.out.printf("Количество собак: %s \n", Dog.getDogsCount());
        System.out.printf("Количество котов: %s \n", Cat.getCatsCount());
        System.out.printf("Количество животных: %s \n", Animal.getTotalAnimalsCount());

    }


}
