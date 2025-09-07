package Lesson21;

public class DoubleValueCalculator extends ValueCalculator<Double>{

    protected DoubleValueCalculator(int arrSize) {
        super(arrSize, "Double");
    }

    @Override
    protected Double[] createArr(int arrSize) {
        return new Double[arrSize];
    }

    @Override
    protected void fillArrayWithOnes() {
        Double[] arr = getArr();
        for (int i = 0; i < arrSize; i ++){
            arr[i] = 1.0;
        }
    }

    @Override
    protected void calculateArray(int startPos, int endPos) {
        Double[] arr = getArr();
        for (int i = startPos; i < endPos; i++) {
            arr[i] *= (Math.sin(0.2f + i / 5f)
                    * Math.cos(0.2f + i / 5f)
                    * Math.cos(0.4f + i / 2f));
        }
    }
}
