package kafka.producer;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Serializable;
import java.util.Properties;
import java.util.Random;

/**
 * @author SeawayLee
 * @create 2018/3/28 16:43
 */
public class MyProducer implements Runnable {
    private Properties prop;
    private KafkaProducer<String, String> producer;
    private String producerName;
    private String topicName;

    public MyProducer(String producerName, String topicName) {
        this.producerName = producerName;
        this.topicName = topicName;
        init();
    }

    public void init() {
        // 生产者配置
        prop = new Properties();
        prop.put("bootstrap.servers", "localhost:9092");
        prop.put("key.serializer", StringSerializer.class);
        prop.put("value.serializer", StringSerializer.class);
        producer = new KafkaProducer<>(prop);
    }

    public void sendData(String topic, String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        try {
            producer.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        Random random = new Random();
        try {

            while (true) {
                this.sendData(this.topicName, this.producerName + ":" + random.nextInt(3), JSON.toJSONString(new MessageData(random.nextInt(10) + "", random.nextInt(999) + "")));
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MessageData implements Serializable {
        public String appId;
        public String module;

        public MessageData(String appId, String module) {
            this.appId = appId;
            this.module = module;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }
    }
}
