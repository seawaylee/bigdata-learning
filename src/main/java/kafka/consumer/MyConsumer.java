package kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;
import java.util.StringJoiner;

/**
 * @author SeawayLee
 * @create 2018/3/28 16:43
 */
public class MyConsumer implements Runnable{
    private Properties prop;
    private KafkaConsumer<String, String> consumer;
    private String consumerName;
    private String topic;

    public void init() {
        // 生产者配置
        prop = new Properties();
        prop.put("bootstrap.servers", "127.0.0.1:9092");
        prop.put("key.deserializer", StringDeserializer.class);
        prop.put("value.deserializer", StringDeserializer.class);
        prop.put("group.id", "CountryCounter");
        // 自动提交partition偏移量
        prop.put("enable.auto.commit", true);
        consumer = new KafkaConsumer<>(prop);
    }

    public MyConsumer(String consumerName,String topic) {
        this.consumerName = consumerName;
        this.topic = topic;
        init();
    }

    @Override
    public void run() {
        this.subscribeData(this.topic);
        this.consumeData();
    }

    public void subscribeData(String topic) {
        consumer.subscribe(Collections.singletonList(topic));
    }

    public void consumeData() {
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(0);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(new StringJoiner(":")
                            .add(this.consumerName)
                            .add(record.topic())
                            .add(String.valueOf(record.partition()))
                            .add(String.valueOf(record.offset()))
                            .add(record.key()).add(record.value()));
                }
            }
        } finally {
            consumer.close();
        }
    }
}
