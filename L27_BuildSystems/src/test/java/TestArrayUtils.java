
import org.example.ArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestArrayUtils {
    int[] normalArr1 = new int[]{1, 2, 3, 4};
    int[] normalArr2 = new int[]{1, 2, 3, 4, 5, 6, 7};
    int[] emptyArr = new int[0];
    int[] nullArr = null;

    @Test
    void testSum() {
        Assertions.assertEquals(10, ArrayUtils.sumArr(this.normalArr1));
        Assertions.assertEquals(28, ArrayUtils.sumArr(this.normalArr2));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ArrayUtils.sumArr(this.emptyArr));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ArrayUtils.sumArr(this.nullArr));
    }

    @Test
    void testAverage() {
        Assertions.assertEquals((double)2.5F, ArrayUtils.average(this.normalArr1), 0.001);
        Assertions.assertEquals((double)4.0F, ArrayUtils.average(this.normalArr2), 0.001);
        Assertions.assertThrows(IllegalArgumentException.class, () -> ArrayUtils.average(this.emptyArr));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ArrayUtils.average(this.nullArr));
    }

    @Test
    void testMax() {
        Assertions.assertEquals(4, ArrayUtils.max(this.normalArr1));
        Assertions.assertEquals(7, ArrayUtils.max(this.normalArr2));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ArrayUtils.max(this.emptyArr));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ArrayUtils.max(this.nullArr));
    }

    @Test
    void testMin() {
        Assertions.assertEquals(1, ArrayUtils.min(this.normalArr1));
        Assertions.assertEquals(1, ArrayUtils.min(this.normalArr2));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ArrayUtils.min(this.emptyArr));
        Assertions.assertThrows(IllegalArgumentException.class, () -> ArrayUtils.min(this.nullArr));
    }
}
