package Lesson12;

public class ArrayDataException extends RuntimeException {

    private final int row;
    private final int column;

    public ArrayDataException(int row, int column, Throwable cause) {
        super(cause);
        this.row = row;
        this.column = column;
    }
    @Override
    public String getMessage() {
        return "В массиве находится некорректные данные в ячейке [" + row + "][" + column + "]! " + getCause();
    }
}
