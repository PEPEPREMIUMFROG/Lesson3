package Lesson12;

public class Main {
    public static void main(String[] args) {
        String[][][] inputs = {
                {{"1", "2", "3", "4"}, {"5", "6", "7", "8"}, {"9", "10", "11", "12"}, {"13", "14", "15"}},
                {{"1", "2", "3", "4"}, {"a", "6", "7", "8"}, {"9", "10", "11", "12"}, {"13", "14", "15", "16"}},
                {{"1", "2", "3", "4"}, {"5", "6", "7", "8"}, {"9", "10", "11", "12"}, {"13", "14", "15", null}},
                {{"1", "2", "3", "4"}, {"5", "6", "7", "8"}, {"9", "10", "11", "12"}, {"13", "14", "15", "16"}},
                {{null}, {"5", "6", "7", "8"}, {"9", "10", "11", "12"}, {"13", "14", "15", "16"}},
                {{"5", "6", "7", "8"}, {"9", "10", "11", "12"}, {"13", "14", "15", "16"}},
                {{"1", "2", "3", "4"}, {"", "6", "7", "8"}, {"9", "10", "11", "12"}, {"13", "14", "15", "16"}},
        };
        
        ArrayValueCalculator calculator = new ArrayValueCalculator();
        for (String[][] input : inputs) {
            try {
                int result = calculator.doCalc(input);
                System.out.printf("Сумма элементов массива: %d \n", result);
            } catch (ArraySizeException | ArrayDataException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}