package com.seawaylee.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.StreamingContext;

import java.util.HashMap;

/**
 * @author SeawayLee
 * @create 2018/5/7 15:55
 */
public class SparkStreamingKafkaDemo {
    public static void main(String[] args) {
        String broker = "localhost:"
        SparkConf sparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreamingKafkaDemo");
        StreamingContext ssc = new StreamingContext(sparkConf, new Duration(5000));
        ssc.checkpoint(".");
        new HashMap<String, String>() {{
            put("metadata.broker.list", "localhost:9092");
            put("group.id", "test-consumer-group");
            put("serializer.class", "kafka.serializer.StringEncoder");
        }};
    }
    public static void createStream() {

    }
}
