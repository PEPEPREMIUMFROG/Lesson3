package Lesson8;

public class Task4 {
    public static void main(String[] args) {
        System.out.println(isPalindrome("EVE"));
        System.out.println(isPalindrome("Allo"));

        System.out.println(isPalindrome(""));
        System.out.println(isPalindrome("A"));
        System.out.println(isPalindrome(null));
    }

    public static boolean isPalindrome(String str) {
        if (str == null){
            return false;
        }

        if (str.isEmpty()) {
            return true;
        }

        int left = 0;
        int right = str.length()-1;
        while (left < right){
            if (str.charAt(left++) != str.charAt(right--)){
                return false;
            }
        }
        return true;
    }
}
