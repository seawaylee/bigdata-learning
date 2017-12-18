package pre_course.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author NikoBelic
 * @create 2017/8/14 21:51
 */
public class CountDownLatchTest
{

    public static void main(String[] args)
    {
        CountDownLatch latch = new CountDownLatch(10);
        ExecutorService es = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 10; i++)
        {
            es.submit(new MThread(i,latch));
        }
        es.shutdown();
        while (latch.getCount() > 0)
        {
            continue;
        }
        System.out.println("Main Thread Done..");
    }
}

class MThread implements Runnable
{
    private int sleep;
    private CountDownLatch latch;

    public MThread(int sleep,CountDownLatch latch)
    {
        this.sleep = sleep;
        this.latch = latch;
    }

    @Override

    public void run()
    {
        try
        {
            Thread.sleep(this.sleep * 1000);
            System.out.println("Shit" + Thread.currentThread().getName());
            latch.countDown();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}