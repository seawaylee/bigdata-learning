package kafka.partitions;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;
import parquet.io.InvalidRecordException;

import java.util.List;
import java.util.Map;

/**
 * @author SeawayLee
 * @create 2018/3/27 21:07
 */
public class CustomPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int partitionsCount = partitions.size();
        if ((keyBytes == null) || (!(key instanceof String))) {
            throw new InvalidRecordException("You must indicate the key!");
        }
        // 指定键的记录永远存储到第一个分区
        if (((String) key).equalsIgnoreCase("iReader")) {
            return 0;
        }
        // 其他记录被散列到其他分区
        return (Math.abs(Utils.murmur2(keyBytes)) % (partitionsCount - 1));
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
