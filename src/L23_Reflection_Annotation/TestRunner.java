package L23_Reflection_Annotation;

import L23_Reflection_Annotation.Annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestRunner {
    public static void start(Class<?> testClass) {
        try {
            Object testInstance = testClass.getDeclaredConstructor().newInstance();

            Method[] allMethods = testClass.getDeclaredMethods();
            Method beforeSuiteMethod = findSingleMethod(testClass, BeforeSuite.class, "@BeforeSuite");
            Method afterSuiteMethod = findSingleMethod(testClass, AfterSuite.class, "@AfterSuite");
            Method[] beforeEachMethods = findMethods(testClass, BeforeEach.class);
            Method[] afterEachMethods = findMethods(testClass, AfterEach.class);
            Method[] testMethods = findMethods(testClass, Test.class);

            executeBeforeSuite(beforeSuiteMethod, testInstance);
            List<Method> enabledTestMethods = getEnabledTestMethods(testMethods);
            executeTestMethods(enabledTestMethods, testInstance, beforeEachMethods, afterEachMethods);
            executeAfterSuite(afterSuiteMethod, testInstance);

        } catch (Exception e) {
            throw new RuntimeException("Error executing tests", e);
        }
    }

    private static void executeBeforeSuite(Method beforeSuiteMethod, Object testInstance) throws Exception {
        if (beforeSuiteMethod != null) {
            System.out.println("Executing @BeforeSuite...");
            beforeSuiteMethod.invoke(testInstance);
            System.out.println();
        }
    }

    private static List<Method> getEnabledTestMethods(Method[] testMethods) {
        List<Method> enabledTestMethods = new ArrayList<>();
        for (Method method : testMethods) {
            Test testAnnotation = method.getAnnotation(Test.class);
            if (testAnnotation.enabled()) {
                enabledTestMethods.add(method);
            } else {
                System.out.println("!!! Test " + method.getName() + " is disabled");
            }
        }
        enabledTestMethods.sort(Comparator.comparingInt(
                m -> m.getAnnotation(Test.class).order()));
        return enabledTestMethods;
    }

    private static void executeTestMethods(List<Method> testMethods, Object testInstance,
                                           Method[] beforeEachMethods, Method[] afterEachMethods) throws Exception {
        System.out.println("Running tests:");
        System.out.println("==================");
        for (Method testMethod : testMethods) {
            executeBeforeEachMethods(beforeEachMethods, testInstance);
            executeTestMethod(testMethod, testInstance);
            executeAfterEachMethods(afterEachMethods, testInstance);
        }
        System.out.println("==================\n");
    }

    private static void executeBeforeEachMethods(Method[] beforeEachMethods, Object testInstance) throws Exception {
        for (Method beforeEachMethod : beforeEachMethods) {
            beforeEachMethod.invoke(testInstance);
        }
    }

    private static void executeTestMethod(Method testMethod, Object testInstance) {
        try {
            testMethod.invoke(testInstance);
        } catch (Exception e) {
            System.err.println("!!! Error in test " + testMethod.getName() + ": " + e.getCause().getMessage());
        }
    }

    private static void executeAfterEachMethods(Method[] afterEachMethods, Object testInstance) throws Exception {
        for (Method afterEachMethod : afterEachMethods) {
            afterEachMethod.invoke(testInstance);
        }
    }

    private static void executeAfterSuite(Method afterSuiteMethod, Object testInstance) throws Exception {
        if (afterSuiteMethod != null) {
            System.out.println("Executing @AfterSuite...");
            afterSuiteMethod.invoke(testInstance);
        }
    }

    private static Method findSingleMethod(Class<?> clazz, Class<? extends Annotation> annotationClass, String annotationName) {
        Method[] methods = findMethods(clazz, annotationClass);
        if (methods.length > 1) {
            throw new RuntimeException("!!! Found more than one method with annotation " + annotationName);
        }
        return methods.length == 1 ? methods[0] : null;
    }

    private static Method[] findMethods(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        List<Method> annotatedMethods = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(annotationClass)) {
                annotatedMethods.add(method);
            }
        }
        return annotatedMethods.toArray(new Method[0]);
    }
}