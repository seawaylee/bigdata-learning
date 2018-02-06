package redis;

import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author NikoBelic
 * @create 2017/12/25 10:54
 */
public class MemoryChipTest
{
    Jedis jedis;
    final long BIG_VALUE_MAX_KEY_SEQUENCE = (long) Math.pow(10,5);
    final long SMALL_VALUE_MAX_KEY_SEQUENCE = (long) Math.pow(10 ,5) * 2;
    byte[] _10_KB = new byte[2 * 1024];
    byte[] _1_KB = new byte[1 * 1024];


    @Before
    public void init()
    {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    @Test
    public void addBigValue()
    {
        jedis.flushDB();
        printInfo("Before Big Size Value Added");
        for (int i = 0; i < BIG_VALUE_MAX_KEY_SEQUENCE; i++)
        {
            jedis.set(Bytes.toBytes(i), _10_KB);
        }
        printInfo("After Big Size Value Added");
    }

    @Test
    public void addSmallValueWithHash()
    {
        String hashKey = "SMALL_VALUE_HASH_KEY";
        jedis.flushDB();
        printInfo("Before Small Size Value Added");
        for (int i = 0; i < SMALL_VALUE_MAX_KEY_SEQUENCE; i++)
        {
            jedis.hset(Bytes.toBytes(hashKey), Bytes.toBytes(i), _1_KB);
        }
        printInfo("After Small Size Value Added");
    }


    @Test
    public void addSmallValueWithKV()
    {
        jedis.flushDB();
        printInfo("Before Small Size Value Added");
        for (int i = 0; i < SMALL_VALUE_MAX_KEY_SEQUENCE; i++)
        {
            jedis.set(Bytes.toBytes(i), _1_KB);
        }
        printInfo("After Small Size Value Added");
    }

    @Test
    public void addSmallValueWithList()
    {
        String listKey = "SMALL_VALUE_LIST_KEY";
        jedis.flushDB();
        printInfo("Before Small Size Value Added");
        for (int i = 0; i < SMALL_VALUE_MAX_KEY_SEQUENCE; i++)
        {
            jedis.hset(Bytes.toBytes(listKey), Bytes.toBytes(i), _1_KB);
        }
        printInfo("After Small Size Value Added");
    }

    private void printInfo(String currentState)
    {
        System.out.println(currentState);
        System.out.println(jedis.info());
    }

}
