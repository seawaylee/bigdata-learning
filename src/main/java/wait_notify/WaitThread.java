package wait_notify;

/**
 * @author NikoBelic
 * @create 2017/4/11 15:49
 */
public class WaitThread implements Runnable
{
    private Object lock;

    public WaitThread(Object lock)
    {
        this.lock = lock;
    }

    @Override
    public void run()
    {
        synchronized (lock)
        {
            System.out.println("WaitThread - " + Thread.currentThread().getName() + " 获取了锁");
            try
            {
                lock.wait();
                System.out.println("WaitThread - " + Thread.currentThread().getName() + " 执行完毕");
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
