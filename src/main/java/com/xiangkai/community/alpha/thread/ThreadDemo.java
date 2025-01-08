package com.xiangkai.community.alpha.thread;

public class ThreadDemo {

    static ThreadDemo t = new ThreadDemo();

    class T1 extends Thread {
        public void run() {
            System.out.println("线程T1执行");
        }
    }

    class T2 extends Thread {
        public void run() {
            T1 t1 = t.new T1();
            t1.start();
            try {
                t1.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("线程T2执行");
        }
    }

    class T3 extends Thread {
        public void run() {
            T2 t2 = t.new T2();
            t2.start();
            try {
                t2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("线程T3执行");
        }
    }

    public static void main(String[] args) {
        t.new T3().start();
    }
}
