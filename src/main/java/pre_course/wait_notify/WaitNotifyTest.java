package pre_course.wait_notify;

/**
 * @author NikoBelic
 * @create 2017/4/11 15:49
 */
public class WaitNotifyTest
{
    public static void main(String[] args) throws InterruptedException
    {
        Object lock = new Object();
        new Thread(new WaitThread(lock)).start();
        Thread.sleep(3000);
        new Thread(new NotifyThread(lock)).start();
    }
}


