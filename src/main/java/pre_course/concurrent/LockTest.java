package pre_course.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author NikoBelic
 * @create 2017/3/31 20:55
 */
public class LockTest
{
    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException
    {
        Thread t1 = new MyThread(lock);
        Thread t2 = new MyThread(lock);

        t1.start();
        t2.start();

        Thread.sleep(5000);

        t2.interrupt();

        //System.out.println(Runtime.getRuntime().availableProcessors());
    }
}

class MyThread extends Thread
{
    private Lock lock;

    public MyThread(Lock lock)
    {
        this.lock = lock;
    }

    @Override
    public void run()
    {
        try
        {
            System.out.println(Thread.currentThread().getName() + " will be locked.");
            lock.lockInterruptibly();
            System.out.println(Thread.currentThread().getName() + " is running...");
            for (; ; )
            {
            }
        } catch (InterruptedException e)
        {
            System.out.println(Thread.currentThread().getName() + "线程被中断");
        } finally
        {
            System.out.println(Thread.currentThread().getName() + " release the lock");
            try
            {
                lock.unlock();
            } catch (Exception e)
            {
                System.out.println(Thread.currentThread().getName() + " 释放锁异常");
            }
        }
    }

}