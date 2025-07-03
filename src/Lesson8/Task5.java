package Lesson8;

import java.util.Random;
import java.util.Scanner;

public class Task5 {

    private static final String[] PLAY_WORDS = {"apple", "orange", "lemon", "banana", "apricot", "avocado",
            "broccoli", "carrot", "cherry", "garlic", "grape", "melon", "leak", "kiwi", "mango", "mushroom",
            "nut", "olive", "pea", "peanut", "pear", "pepper", "pineapple", "pumpkin", "potato"};

    private static final int MAX_LENGTH = 15;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String hiddenWord = pickRandomWord();

        while (true) {
            System.out.println("Enter a hidden word:");
            String answer = scanner.nextLine();

            if (answer.equals(hiddenWord)) {
                System.out.println("You are win!");
                break;
            } else {
                printCharsGuessed(answer, hiddenWord);
            }
        }
    }


    private static void printCharsGuessed(String answer, String hiddenWord) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < MAX_LENGTH; i++) {
            char currentCharInHidden = i < hiddenWord.length() ? hiddenWord.charAt(i) : '#';
            char currentCharInAnswer = i < answer.length() ? answer.charAt(i) : '#';

            if (currentCharInHidden == currentCharInAnswer) {
                result.append(currentCharInAnswer);
            } else {
                result.append('#');
            }
        }

        while (result.length() < MAX_LENGTH) {
            result.append('#');
        }
        System.out.println(result);
    }

    private static String pickRandomWord() {
        Random random = new Random();
        int indexOfHiddenWord = random.nextInt(PLAY_WORDS.length);
        return PLAY_WORDS[indexOfHiddenWord];
    }
}
