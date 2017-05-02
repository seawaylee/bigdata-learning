package wait_notify;

/**
 * @author NikoBelic
 * @create 2017/4/11 15:51
 */
public class NotifyThread implements Runnable
{
    private Object lock;

    public NotifyThread(Object lock)
    {
        this.lock = lock;
    }

    @Override
    public void run()
    {
        synchronized (lock)
        {
            System.out.println("NotifyThread - " + Thread.currentThread().getName() + " 获取了锁");
            lock.notify();
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            System.out.println("NotifyThread - " + Thread.currentThread().getName() + "执行完毕");
        }
    }
}
