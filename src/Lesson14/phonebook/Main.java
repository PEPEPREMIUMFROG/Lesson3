package Lesson14.phonebook;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Phonebook phonebook = new Phonebook();

        phonebook.add(new Record("Иван Иванов", "+7-999-123-45-67"));
        phonebook.add(new Record("Петр Петров", "+7-999-123-45-68"));
        phonebook.add(new Record("Сергей Сергеев", "+7-999-123-45-69"));
        phonebook.add(new Record("Иван Иванов", "+7-999-123-45-66"));

        Record firstIvan = phonebook.find("Иван Иванов");
        System.out.println("Первая запись Ивана: " + firstIvan);

        List<Record> allIvans = phonebook.findAll("Иван Иванов");
        System.out.println("Все записи Ивана:");
        for (Record record : allIvans) {
            System.out.println("  " + record);
        }

        Record unknown = phonebook.find("Алексей Алексеев");
        System.out.println("Результат поиска несуществующей записи: " + unknown);
    }


}

