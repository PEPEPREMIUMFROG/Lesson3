package L24_Test_Framework.project.tests;

import L24_Test_Framework.framework.Marker.Test;
import L24_Test_Framework.framework.assertions.AssertException;
import L24_Test_Framework.framework.assertions.Assertions;
import L24_Test_Framework.project.function.Calculator;

public class RecursiveEqualsTest {

    @Test
    public void testCalculatorsRecursivelyEquals() throws AssertException {
        Calculator c1 = new Calculator();
        Calculator c2 = new Calculator();
        Assertions.equalRecursively(c1, c2);
    }

    @Test
    public void testEqualRecursivelyPrimitives() throws AssertException {
        int a = 5;
        int b = 5;
        Assertions.equalRecursively(a, b);
    }

    @Test
    public void testEqualRecursivelyStrings() throws AssertException {
        String str1 = "Hello";
        String str2 = "Hello";
        Assertions.equalRecursively(str1, str2);
    }

    @Test
    public void testEqualRecursivelyPrimitiveArrays() throws AssertException {
        int[] arr1 = {1, 2, 3, 4, 5};
        int[] arr2 = {1, 2, 3, 4, 5};
        Assertions.equalRecursively(arr1, arr2);
    }

    @Test
    public void testEqualRecursively2DPrimitiveArrays() throws AssertException {
        int[][] arr1 = {{1, 2}, {3, 4}};
        int[][] arr2 = {{1, 2}, {3, 4}};
        Assertions.equalRecursively(arr1, arr2);
    }

    @Test
    public void testEqualRecursivelyObjectArrays() throws AssertException {
        String[] arr1 = {"Hello", "World"};
        String[] arr2 = {"Hello", "World"};
        Assertions.equalRecursively(arr1, arr2);
    }

    @Test
    public void testEqualRecursivelyObjects() throws AssertException {
        Person person1 = new Person("John", 25);
        Person person2 = new Person("John", 25);
        Assertions.equalRecursively(person1, person2);
    }

    @Test
    public void testEqualRecursivelyArrayOfObjects() throws AssertException {
        Person[] people1 = {
                new Person("John", 25),
                new Person("Jane", 30)
        };
        Person[] people2 = {
                new Person("John", 25),
                new Person("Jane", 30)
        };
        Assertions.equalRecursively(people1, people2);
    }

    @Test
    public void testEqualRecursivelyMixedArrays() throws AssertException {
        Object[] arr1 = {1, "Hello", new Person("John", 25)};
        Object[] arr2 = {1, "Hello", new Person("John", 25)};
        Assertions.equalRecursively(arr1, arr2);
    }

    @Test
    public void testEqualRecursivelyBothNull() throws AssertException {
        Assertions.equalRecursively(null, null);
    }

    @Test
    public void testEqualRecursivelyDifferentPrimitives() throws AssertException {
        int a = 5;
        int b = 10;
        Assertions.equalRecursively(a, b);
    }

    @Test
    public void testEqualRecursivelyDifferentArrays() throws AssertException {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        Assertions.equalRecursively(arr1, arr2);
    }

    @Test
    public void testEqualRecursivelyDifferentObjects() throws AssertException {
        Person person1 = new Person("John", 25);
        Person person2 = new Person("Jane", 30);
        Assertions.equalRecursively(person1, person2);
    }

    @Test
    public void testEqualRecursivelyOneNull() throws AssertException {
        Person person = new Person("John", 25);
        Assertions.equalRecursively(person, null);
    }

    public record Person(String name, int age) {
    }

}

