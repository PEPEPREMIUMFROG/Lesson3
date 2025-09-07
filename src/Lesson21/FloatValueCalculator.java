package Lesson21;

public class FloatValueCalculator extends ValueCalculator<Float> {

    protected FloatValueCalculator(int arrSize) {
        super(arrSize, "Float");
    }

    @Override
    protected Float[] createArr(int arrSize) {
        return new Float[arrSize];
    }

    @Override
    protected void fillArrayWithOnes() {
        Float[] arr = getArr();
        for (int i = 0; i < arrSize; i ++){
            arr[i] = 1.0f;
        }
    }

    @Override
    protected void calculateArray(int startPos, int endPos) {
        Float[] arr = getArr();
        for (int i = startPos; i < endPos; i++) {
            arr[i] *= (float) (Math.sin(0.2f + i / 5f)
                    * Math.cos(0.2f + i / 5f)
                    * Math.cos(0.4f + i / 2f));
        }
    }
}
