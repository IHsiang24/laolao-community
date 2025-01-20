package com.xiangkai.community.alpha.container.collection.queue;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class QueueExample {

    public static void main(String[] args) {
        LinkedList<String> queue = new LinkedList<>();
        queue.addFirst("a");
        queue.addFirst("c");
        queue.addLast("b");
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o));
    }
}
