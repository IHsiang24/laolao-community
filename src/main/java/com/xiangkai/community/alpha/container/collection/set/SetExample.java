package com.xiangkai.community.alpha.container.collection.set;

import java.util.*;

public class SetExample {

    public static void main(String[] args) {
        HashSet<Integer> hashSet = new HashSet<>();
        hashSet.add(1);

        TreeSet<Integer> set = new TreeSet<>(new MyComparator());
        set.add(1);

        testTreeMap();
    }

    static class MyComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    }

    public static void testTreeMap() {
        TreeMap<Integer, String> treeMap = new TreeMap<>(new MyComparator());
        treeMap.put(1, "one");
        treeMap.put(3, "three");
        treeMap.put(2, "two");
        for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
