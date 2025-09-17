package L24_Test_Framework.framework.assertions;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class Assertions {

    public static <T> void equals(T expected, T actual) throws AssertException {
        boolean success = Objects.equals(expected, actual);
        throw new AssertException(new AssertEqualResult<>(actual, expected, success));
    }

    public static void contains(String input, String expected) throws AssertException {
      boolean success = input != null && expected != null && input.contains(expected)  ;
        throw new AssertException(new AssertContainsResult(success, input, expected));

    }

    public static <T> void contains(T[] current, T[] toContain) throws AssertException {
        boolean success = false;
        String errorMessage = "";
        if (current == null && toContain == null || toContain.length ==0) {
            success = true;
        } else if (current == null || toContain == null) {
            success = false;
            errorMessage = "One of arrays is null";
        } else if (toContain.length > current.length) {
            success = false;
        } else {
            boolean found = false;
            for (int i = 0; i <= current.length - toContain.length && !found; i++) {
                boolean match = true;
                for (int j = 0; j < toContain.length; j++) {
                    if (!Objects.equals(current[i + j], toContain[j])) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    success = true;
                    found = true;
                }
            }
        }
        throw new AssertException(new AssertArrayContainsResult(success, current, toContain, errorMessage));
    }

    public static <T> void equalRecursively(T expected, T actual) throws AssertException {
        boolean success = deepEquals(expected, actual);
        throw new AssertException(new AssertRecursiveEqualResult(expected, actual, success));
    }

    private static boolean deepEquals(Object expected, Object actual) {
        if (expected == actual) {
            return true;
        }
        if (expected == null || actual == null) {
            return false;
        }
        Class<?> expectedClass = expected.getClass();
        Class<?> actualClass = actual.getClass();
        if (!expectedClass.equals(actualClass)) {
            return false;
        }
        if (expectedClass.isArray()) {
            return deepArrayEquals(expected, actual);
        }
        if (expectedClass.isPrimitive() || expected instanceof String ||
                expected instanceof Number || expected instanceof Boolean ||
                expected instanceof Character) {
            return Objects.equals(expected, actual);
        }
        return deepObjectEquals(expected, actual);
    }

    private static boolean deepArrayEquals(Object expectedArray, Object actualArray) {
        if (expectedArray instanceof Object[] && actualArray instanceof Object[]) {
            Object[] expected = (Object[]) expectedArray;
            Object[] actual = (Object[]) actualArray;
            if (expected.length != actual.length) {
                return false;
            }
            for (int i = 0; i < expected.length; i++) {
                if (!deepEquals(expected[i], actual[i])) {
                    return false;
                }
            }
            return true;
        } else if (expectedArray instanceof byte[] && actualArray instanceof byte[]) {
            return Arrays.equals((byte[]) expectedArray, (byte[]) actualArray);
        } else if (expectedArray instanceof short[] && actualArray instanceof short[]) {
            return Arrays.equals((short[]) expectedArray, (short[]) actualArray);
        } else if (expectedArray instanceof int[] && actualArray instanceof int[]) {
            return  Arrays.equals((int[]) expectedArray, (int[]) actualArray);
        } else if (expectedArray instanceof long[] && actualArray instanceof long[]) {
            return Arrays.equals((long[]) expectedArray, (long[]) actualArray);
        } else if (expectedArray instanceof char[] && actualArray instanceof char[]) {
            return Arrays.equals((char[]) expectedArray, (char[]) actualArray);
        } else if (expectedArray instanceof float[] && actualArray instanceof float[]) {
            return Arrays.equals((float[]) expectedArray, (float[]) actualArray);
        } else if (expectedArray instanceof double[] && actualArray instanceof double[]) {
            return Arrays.equals((double[]) expectedArray, (double[]) actualArray);
        } else if (expectedArray instanceof boolean[] && actualArray instanceof boolean[]) {
            return Arrays.equals((boolean[]) expectedArray, (boolean[]) actualArray);
        }
        return false;
    }

    private static boolean deepObjectEquals(Object expected, Object actual) {
        try {
            Class<?> aClass = expected.getClass();
            while (aClass != null && !aClass.equals(Object.class)) {
            Field[] fields = aClass.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object expectedValue = field.get(expected);
                    Object actualValue = field.get(actual);
                    if (!deepEquals(expectedValue, actualValue)) {
                        return false;
                    }
                }
                aClass = aClass.getSuperclass();
            }
            return true;
        } catch (IllegalAccessException e) {
            return Objects.equals(expected, actual);
        }
    }
}
