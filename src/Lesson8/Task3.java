package Lesson8;

import java.util.Arrays;

public class Task3 {
    public static void main(String[] args) {
        System.out.println(reverseString("abc"));

        System.out.println(reverseString(null));
        System.out.println(reverseString(""));
        System.out.println(reverseString("A"));


    }

    public static String reverseString(String str){
        if (str == null || str.isEmpty() ){
            return "";
        }

        char[] chars = str.toCharArray();
        int left = 0;
        int right = chars.length-1;
        while (left < right){
            char temp;
            temp = chars[left];
            chars[left++] = chars[right];
            chars[right--] = temp;
        }
        return new String(chars);
    }
}
