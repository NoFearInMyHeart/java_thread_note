package com.wkcto.lock.reentrant;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 *tryLock(long time, TimeUnit unit) 的基本使用
 */
public class Test07 {
    static class TimeLock implements Runnable{
        private static ReentrantLock lock = new ReentrantLock();    //定义锁对象

        @Override
        public void run() {
            try {
                if ( lock.tryLock(3, TimeUnit.SECONDS) ){       //获得锁返回true
                    System.out.println(Thread.currentThread().getName() + "获得锁,执行耗时任务");
//                    Thread.sleep(4000);         //假设Thread-0线程先持有锁,完成任务需要4秒钟,Thread-1线程尝试获得锁,Thread-1线程在3秒内还没有获得锁的话,Thread-1线程会放弃
                    Thread.sleep(2000);          //假设Thread-0线程先持有锁,完成任务需要2秒钟,Thread-1线程尝试获得锁,Thread-1线程会一直尝试,在它约定尝试的3秒内可以获得锁对象
                }else {         //没有获得锁
                    System.out.println(Thread.currentThread().getName() + "没有获得锁");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock.isHeldByCurrentThread()){
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        TimeLock timeLock = new TimeLock();

        Thread t1 = new Thread(timeLock);
        Thread t2 = new Thread(timeLock);
        t1.start();
        t2.start();
    }
}
