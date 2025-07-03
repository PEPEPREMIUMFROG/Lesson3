package Lesson8;

public class Task1 {
    public static void main(String[] args) {

        String someStr = "Abcdeafaghab";
        System.out.println(findSymbolOccurance(someStr, 'a'));
        System.out.println(findSymbolOccurance(someStr, 'b'));

        System.out.println(findSymbolOccurance(null,'a'));
        System.out.println(findSymbolOccurance("",'a'));
    }

    public static int findSymbolOccurance(String inputStr, char requiredChar) {
        if (inputStr == null || inputStr.isEmpty()) {
            return 0;
        }

        int counter = 0;
        for (int i = 0; i < inputStr.length(); i++) {
            char current = inputStr.charAt(i);
            if (current == requiredChar) {
                counter++;
            }
        }
        return counter;
    }
}
