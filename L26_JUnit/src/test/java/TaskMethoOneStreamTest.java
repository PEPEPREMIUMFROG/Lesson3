import org.example.Task;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TaskMethoOneStreamTest {


    private Task task;

    @BeforeAll
    static  void  staticInitialization(){
        System.out.println("Static initialization completed");
    }

    @AfterAll
    static void cleanStaticResources(){
        System.out.println("Static resources was cleaned");
    }

    @AfterEach
    void cleanResources(){
        System.out.println("Resources was cleaned");
    }

    @BeforeEach
    void setUp() {
        task = new Task();
        System.out.println("Resources was initialized");
    }

    @Test
    void testNormalCase() {
        int[] input = {1, 2, 3, 4, 5, 6, 7};
        int[] result = task.methoOneWithStreams(input);
        int[] expected = {5, 6, 7};
        assertArrayEquals(expected, result);

    }

    @Test
    void testNoFourInArray() throws RuntimeException {
        int[] input = {0, 1, 2};
        Exception caughtEx = assertThrows(RuntimeException.class, () -> task.methoOneWithStreams(input));
        assertTrue(caughtEx.getMessage().contains("array dont have 4"));
    }

    @Test
    void testOneFourArray() {
        int[] input = {4};
        int[] result = task.methoOneWithStreams(input);
        int[] expected = {};
        assertArrayEquals(expected, result);
    }

    @Test
    void testMultipleFourArray() {
        int[] input = {1, 1, 4, 1, 4, 2, 3};
        int[] result = task.methoOneWithStreams(input);
        int[] expected = {2, 3};
        assertArrayEquals(expected, result);

    }
}
