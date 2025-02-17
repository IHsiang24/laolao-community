package com.xiangkai.community.alpha.java_basic_syntax;

public class ArrayExample {

    public static void main(String[] args) {
        int[] arr1 = { 1, 2, 3};
        int arr2[] = { 1, 2, 3};

        int[] arr3 = new int[]{ 1, 2, 3};
        int arr4[] = new int[]{ 1, 2, 3};

        int[] arr5 = new int[3];

        char[] ch = { 'a', 'b', 'c' };
        String s = "abc";
        int a = 10;
        exchange(s, ch, a);
        System.out.println(s);
        System.out.println(ch);
        System.out.println(a);
    }

    public static void exchange(String s, char[] ch, int a) {
        s = "gbc";
        ch[0] = 'g';
        a = 20;
    }
}
