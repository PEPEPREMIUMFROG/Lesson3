import org.example.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskMethodTwoTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        System.out.println("Resources was initialized");
    }

    @Test
    void testNormalCase(){
        int[]input = {1,1,1,4,4,4};
        assertTrue(task.methodTwo(input));
    }

    @Test
    void testOnlyFour(){
        int[]input = {4,4,4,4,4};
        assertFalse(task.methodTwo(input));
    }
    @Test void testOnlyOne(){
        int[]input = {1,1,1,1,1,1};
        assertFalse(task.methodTwo(input));
    }

    @Test
    void testNotOnlyOneAndFour(){
        int[]input = {1,4,1,4,1,4,2};
        assertFalse(task.methodTwo(input));
    }
    @Test
    void testEmptyArr(){
        int[]input = {};
        assertFalse(task.methodTwo(input));
    }


}
