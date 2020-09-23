package com.k.juc.testMethods;

import lombok.Data;

/**
 * @Description TODO
 * @Author 王一鸣
 * @Date 2020/5/30 9:45
 **/
public class VolatileTest {
    public static void main(String[] args) {
        ThreadDemo threadDemo = new ThreadDemo();
        Thread thread = new Thread(threadDemo);
        thread.start();
        while (true){
            if (threadDemo.isFlag()){
                System.out.println("主线程读取到的flag = " + threadDemo.isFlag());
                break;
            }
        }
    }
}

/**
 * 这个线程是用来修改flag的值的
 */
@Data
class ThreadDemo implements Runnable{
    public  boolean flag = false;
    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flag = true;
        System.out.println("ThreadDemo线程修改后的flag = " + isFlag());
    }
}
