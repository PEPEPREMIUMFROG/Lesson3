package Lesson12;

public class ArrayValueCalculator {
    public static int doCalc(String[][] array) throws ArrayDataException, ArraySizeException {
        if (array.length != 4) {
            throw new ArraySizeException("Неверный размер массива");
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i].length != 4 || array[i] == null) {
                throw new ArraySizeException("Неверный размер массива");
            }
        }
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                try {
                    sum += Integer.parseInt(array[i][j]);
                } catch (NumberFormatException e) {
                    throw new ArrayDataException(i+1, j+1, e);
                }
            }
        }
        return sum;
    }
}