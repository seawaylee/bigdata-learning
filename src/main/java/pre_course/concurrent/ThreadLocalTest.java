package pre_course.concurrent;

/**
 * @author NikoBelic
 * @create 2017/8/20 23:50
 */
public class ThreadLocalTest
{
    static ThreadLocal<Integer> var = new ThreadLocal<>();

    public static void main(String[] args)
    {
        var.set(5);
        for (int i = 0; i < 10; i++)
        {
            new Thread(new TLTHread()).start();
        }
        System.out.println(Thread.currentThread().getName() + "--" + var.get());
    }

    static class TLTHread implements Runnable
    {
        @Override
        public void run()
        {
            System.out.println(Thread.currentThread().getName() + "--" + var.get());
            var.set(var.get() + 1);
        }
    }

}

