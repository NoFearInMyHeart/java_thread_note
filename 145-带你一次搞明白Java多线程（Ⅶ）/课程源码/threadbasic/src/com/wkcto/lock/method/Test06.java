package com.wkcto.lock.method;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *boolean hasWaiters(Condition condition)   查询是否有线程正在等待指定以Condition条件
 */
public class Test06 {
    static ReentrantLock lock = new ReentrantLock();    //创建锁对象
    static Condition condition = lock.newCondition();       //返回锁定的条件

    static void sm(){
        try {
            lock.lock();
            System.out.println( "是否有线程正在等待当前Condition条件? " + lock.hasWaiters(condition) + " -- waitqueuelenth: " + lock.getWaitQueueLength(condition) );
            System.out.println(Thread.currentThread().getName() + " waing....");
            condition.await(new Random().nextInt(1000), TimeUnit.MILLISECONDS);  //超时后会自动唤醒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println( Thread.currentThread().getName() +"超时唤醒, 是否有线程正在等待当前Condition条件? " + lock.hasWaiters(condition) + " -- waitqueuelenth: " + lock.getWaitQueueLength(condition) );
//            System.out.println(Thread.currentThread().getName() + " unlock...");
            lock.unlock();
        }

    }

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                sm();
            }
        };
        //开启10个线程,调用sm()方法
        for (int i = 0; i < 10; i++) {
            new Thread(runnable).start();
        }
    }
}
