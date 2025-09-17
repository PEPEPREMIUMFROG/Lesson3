package L24_Test_Framework.project.tests;

import L24_Test_Framework.framework.Marker.Test;
import L24_Test_Framework.framework.assertions.AssertException;
import L24_Test_Framework.framework.assertions.Assertions;

public class ArrContainsTest {

    @Test
    public void testArrayContainsSuccess() throws AssertException {
        Integer[] array = {5, 9, 1, 2, 3, 10};
        Integer[] subArray = {1, 2, 3};
        Assertions.contains(array, subArray);
    }

    @Test
    public void testArrayContainsFail() throws AssertException {
        Integer[] array = {5, 9, 1, 2, 3, 10};
        Integer[] subArray = {1, 3, 2};
        Assertions.contains(array, subArray);
    }

    @Test
    public void testStringArrayContains() throws AssertException {
        String[] words = {"hello", "world", "java", "test"};
        String[] subWords = {"world", "java"};
        Assertions.contains(words, subWords);
    }

    @Test
    public void testEmptySubArray() throws AssertException {
        Integer[] array = {1, 2, 3};
        Integer[] empty = {};
        Assertions.contains(array, empty);
    }

    @Test
    public void testNullArrays() throws AssertException {
        Integer[] array = null;
        Integer[] subArray = null;
        Assertions.contains(array, subArray);
    }
}
