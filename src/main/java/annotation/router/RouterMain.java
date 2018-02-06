package annotation.router;

import java.util.Random;

/**
 * @author NikoBelic
 * @create 2018/2/6 15:47
 */
public class RouterMain {
    private static Random random = new Random();

    private static String getRandomStr(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IllegalAccessException {
        for (int i = 0; i < 10; i++) {
            GroupServiceRouter groupServiceRouter = new GroupServiceRouter();
            groupServiceRouter.get(getRandomStr(8)).print();
        }

    }
}
