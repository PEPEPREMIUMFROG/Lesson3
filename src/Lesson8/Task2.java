package Lesson8;

public class Task2 {
    public static void main(String[] args) {

        System.out.println(findWordPosition("Apollo", "pollo"));
        System.out.println(findWordPosition("Apple", "Plant"));

        System.out.println(findWordPosition("Apple", ""));
        System.out.println(findWordPosition("", "Plant"));
        System.out.println(findWordPosition("pollo", "pollo"));
        System.out.println(findWordPosition(null, null));

    }
    public static int findWordPosition(String source, String target) {
        if (source == null || target==null){
            return -1;
        }

        if (target.isEmpty()) {
            return 0;
        }

        if (source.isEmpty() || target.length() > source.length()) {
            return -1;
        }

        int sourceLength = source.length();
        int targetLength = target.length();

        for (int startIndex = 0; startIndex <= sourceLength - targetLength; startIndex++) {
            boolean foundMatch = true;
            for (int charIndex = 0; charIndex < targetLength; charIndex++) {
                if (source.charAt(startIndex + charIndex) != target.charAt(charIndex)) {
                    foundMatch = false;
                    break;
                }
            }
            if (foundMatch) {
                return startIndex;
            }
        }
        return -1;

    }
}
