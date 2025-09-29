import jdk.jfr.Description;
import org.example.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class TaskMethodOneTest {

    private Task task;
    private Method methodOne;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        task = new Task();
        methodOne = Task.class.getDeclaredMethod("methodOne", int[].class);
        methodOne.setAccessible(true);
        System.out.println("Resources was initialized");
    }

    private int[] callMethodOne(int[] input) {
        try {
            return (int[]) methodOne.invoke(task, (Object) input);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                throw new RuntimeException(cause);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testNormalCase() {
        int[] input = {1, 2, 3, 4, 5, 6, 7};
        int[] result = callMethodOne(input);
        int[] expected = {5, 6, 7};
        assertArrayEquals(expected, result);

    }


    @Test()
    void testNoFourInArray() throws RuntimeException {
        int[] input = {0, 1, 2};
        Exception caughtEx = assertThrows(RuntimeException.class, () -> callMethodOne(input));
        assertTrue(caughtEx.getMessage().contains("array dont have 4"));
    }

    @Test
    void testOneFourArray() {
        int[] input = {4};
        int[] result = callMethodOne(input);
        int[] expected = {};
        assertArrayEquals(expected, result);
    }

    @Test
    void testMultipleFourArray() {
        int[] input = {1, 1, 4, 1, 4, 2, 3};
        int[] result = callMethodOne(input);
        int[] expected = {2, 3};
        assertArrayEquals(expected, result);

    }


}
