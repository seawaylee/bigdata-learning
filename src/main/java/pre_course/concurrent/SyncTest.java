package pre_course.concurrent;

/**
 * @author NikoBelic
 * @create 2017/4/19 19:17
 */
public class SyncTest
{
    public static void main(String[] args) throws InterruptedException
    {
        Object lock = new Object();
        Thread t1 = new Thread(new SThread(lock));
        Thread t2 = new Thread(new SThread(lock));
        t1.start();
        t2.start();

        Thread.sleep(20000);
        synchronized (lock)
        {
            lock.notify();
        }
        Thread.sleep(20000);
        /*
        实验结论：
        1. lock.wait(),lock.notify()必须在监视器下执行，否则会跑出异常
        2. lock.wait()会释放锁。lock.notify()会随机唤醒一个等待的线程

         */
    }
}

class SThread implements Runnable
{
    private Object lock;

    public SThread(Object lock)
    {
        this.lock = lock;
    }

    @Override
    public void run()
    {
        synchronized (lock)
        {

            try
            {
                System.out.println(Thread.currentThread().getName() + " is running...");
                lock.wait();
                System.out.println(Thread.currentThread().getName() + " is shutting down");
                lock.notify();
                while (true)
                {

                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
