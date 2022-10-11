package com.wkcto.lock.method;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * int getWaitQueueLength(Condition condition)  返回在Condition条件上等待的线程预估数
 */
public class Test04 {
    static  class  Service{
        private ReentrantLock lock = new ReentrantLock();       //定义锁对象
        private Condition condition = lock.newCondition();      //返回锁给定的Condition
        public void waitMethod(){
            try {
                lock.lock();
                System.out.println( Thread.currentThread().getName() + " 进入等待前, 现在该condtion条件上等待的线程预估数: " + lock.getWaitQueueLength(condition));
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
        }
        public void  notifyMethod(){
            try {
                lock.lock();
                condition.signalAll();      //唤醒所有的等待
                System.out.println("唤醒所有的等待后,condtion条件上等待的线程预估数: " + lock.getWaitQueueLength(condition));
            }finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Service service = new Service();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                service.waitMethod();
            }
        };
        //创建10个线程调用waitMethod()
        for (int i = 0; i < 10; i++) {
            new Thread(r).start();
        }

        Thread.sleep(2000);
        service.notifyMethod();     //唤醒所有的等待
    }
}
