package kafka;

import kafka.consumer.MyConsumer;
import kafka.producer.MyProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author SeawayLee
 * @create 2018/3/28 20:01
 */
public class KafkaMain {
    public static void main(String[] args) {
        ThreadPoolExecutor producerPool = new ThreadPoolExecutor(5, 100, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        ThreadPoolExecutor consumerPool = new ThreadPoolExecutor(5, 100, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
        String topic = "my_topic";
        List<MyConsumer> myConsumerList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            MyProducer producer = new MyProducer("Producer-" + i, topic);
            producerPool.execute(new Thread(producer));
            MyConsumer consumer = new MyConsumer("Consumer-" + i, topic);
            myConsumerList.add(consumer);
            consumerPool.execute(new Thread(consumer));
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Starting exit")));

    }
}
