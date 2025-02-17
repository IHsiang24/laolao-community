package com.xiangkai.community.alpha.java_basic_syntax;

import java.util.ArrayList;
import java.util.List;

public class StringExample {

    public static void main(String[] args) {
        String str1 = "hello";
        String str2 = "he" + new String("llo");
        System.out.println("str1 == str2: " + (str1 == str2));

        String str3 = "hello";
        String str4 = new String("hello");
        System.out.println("str3 == str4: " + (str3 == str4));

        List<String> list1 = new ArrayList<String>();
        list1.add("a");
        list1.add("b");
        list1.add("c");

        List<String> list2 = new ArrayList<String>();
        list2.add("a");
        list2.add("b");
        list2.add("c");

        System.out.println("list1 == list2: " + (list1 == list2));
        System.out.println("list1 equals list2: " + (list1.equals(list2)));

    }
}
