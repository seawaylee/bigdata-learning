package storm.word_count;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author NikoBelic
 * @create 2017/11/29 00:26
 */
public class WordCountTopology
{
    public static class MySpout extends BaseRichSpout
    {

        SpoutOutputCollector collector;
        String[] sourceData = new String[]{
                "Nowadays, going to gym after work is most people’s choice, because they need to be keep fit.",
                "The young blue and white collar sit in their offices all the daytime and they lack of exercise,",
                "most of them are in the sub-health state.",
                "They realize the importance of taking exercise and more gyms have been opened to meet their needs.",
                "In my opinion, we can not only exercise our body, but also to have fun.",
                "The gym provides people a place to make friends.",
                "They can find someone who has the same interest with them, and then just talk to them.",
                "They can forget about the annoyance and just relax themselves.",
                "Some girls like to do yoga while some boys like to play badmiton.",
                "They can choose whatever they like, which is good for keeping good mood."

        };

        /**
         * 初始化方法
         *
         * @Author SeawayLee
         * @Date 2017/11/29 00:41
         */
        @Override
        public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector)
        {
            this.collector = spoutOutputCollector;
        }

        /**
         * Storm框架持续无限调用此方法
         *
         * @Author SeawayLee
         * @Date 2017/11/29 00:42
         */
        @Override
        public void nextTuple()
        {
            collector.emit(new Values(sourceData[new Random().nextInt(sourceData.length)]));
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        /**
         * 定义emit出去的value的field名称
         *
         * @Author SeawayLee
         * @Date 2017/11/29 00:48
         */
        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer)
        {
            outputFieldsDeclarer.declare(new Fields("sentence"));
        }
    }

    public static class MySplitBolt extends BaseRichBolt
    {

        OutputCollector collector;

        /**
         * 初始化方法
         *
         * @Author SeawayLee
         * @Date 2017/11/29 00:46
         */
        @Override
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector)
        {
            this.collector = outputCollector;
        }

        /**
         * 被Storm框架持续无限调用此方法
         *
         * @Author SeawayLee
         * @Date 2017/11/29 00:46
         */
        @Override
        public void execute(Tuple tuple)
        {
            String line = tuple.getString(0);
            String[] words = line.split(" ");
            for (String word : words)
            {
                collector.emit(new Values(word, 1));
            }
        }

        /**
         * 定义emit出去的value的field名称
         *
         * @Author SeawayLee
         * @Date 2017/11/29 00:49
         */
        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer)
        {
            outputFieldsDeclarer.declare(new Fields("word", "num"));
        }
    }

    public static class MyCountBold extends BaseRichBolt
    {
        OutputCollector collector;
        Map<String, Integer> map = new HashMap<>();

        @Override
        public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector)
        {
            this.collector = outputCollector;
        }

        @Override
        public void execute(Tuple tuple)
        {
            String word = tuple.getString(0);
            Integer count = tuple.getInteger(1);
            System.out.println(Thread.currentThread().getId() + "   word:" + word);
            map.put(word, map.containsKey(word) ? map.get(word) + count : count);
            System.out.println("count:" + map);
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer)
        {
            // 不输出
        }
    }

    public static void main(String[] args)
    {
        // 1. 准备Topology
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("mySpout", new MySpout(), 2);
        topologyBuilder.setBolt("splitBolt", new MySplitBolt(), 2).shuffleGrouping("mySpout");
        topologyBuilder.setBolt("countBolt", new MyCountBold(), 4).fieldsGrouping("splitBolt", new Fields("word"));

        // 2. 创建Config，指定当前Topology需要的Worker数量
        Config config = new Config();
        config.setNumWorkers(2);

        // 3. 提交任务   本地模式 和 集群模式
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("myWordCount", config, topologyBuilder.createTopology());
        //StormSubmitter.submitTopology("myWordCount", config, topologyBuilder.createTopology());

    }
}
