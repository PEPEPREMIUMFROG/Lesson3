package Lesson19;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public class Task3 {
    public static void main(String[] args) {
        Collection<String>strings = List.of("Hello", "hello", "m", ",,,", "," ,"wordl", "!");
        Predicate<String> moreThan2Chars = s -> s.length() >2;
        System.out.println(filterStrings(strings, moreThan2Chars));
    }
    public static String filterStrings(Collection<String> strings,
                                       Predicate<String> predicate)
    {
        return strings.stream()
                .filter(predicate)
                .reduce((s1,s2)-> s1 + "|" + s2)
                .orElse("");
    }
}
