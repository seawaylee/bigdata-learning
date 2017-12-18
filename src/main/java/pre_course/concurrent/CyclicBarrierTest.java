package pre_course.concurrent;

import java.util.concurrent.*;

/**
 * @author NikoBelic
 * @create 2017/8/14 22:02
 */
public class CyclicBarrierTest
{
    public static void main(String[] args)
    {
        CyclicBarrier cyclic = new CyclicBarrier(10);
        ExecutorService es = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 10; i++)
        {
            es.submit(new CBThread(i, cyclic));
        }
        es.shutdown();
        System.out.println("Main Thread Done..");
    }
}

class CBThread implements Runnable
{
    private int sleep;
    private CyclicBarrier barrier;

    public CBThread(int sleep, CyclicBarrier barrier)
    {
        this.sleep = sleep;
        this.barrier = barrier;
    }

    @Override

    public void run()
    {
        try
        {
            Thread.sleep(this.sleep * 1000);
            barrier.await();
            System.out.println("Shit" + Thread.currentThread().getName());
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (BrokenBarrierException e)
        {
            e.printStackTrace();
        }
    }
}