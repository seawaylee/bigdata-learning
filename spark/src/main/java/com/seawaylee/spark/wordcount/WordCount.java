package com.seawaylee.spark.wordcount;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

/**
 * @author SeawayLee
 * @create 2018/5/2 19:56
 */
public class WordCount {
    public static void main(String[] args)
    {
        String logFile = "file:////Users/lixiwei-mac/app/spark-1.6.0-bin-hadoop2.6/bin/sparkR.cmd";
        SparkSession spark = SparkSession.builder().appName("Simple Application").getOrCreate();
        Dataset<String> logData = spark.read().textFile(logFile).cache();
        long numAs = logData.filter(s -> s.contains("a")).count();
        long numBs = logData.filter(s -> s.contains("b")).count();
        System.out.println("Lines with a:" + numAs + ", lines with b:" + numBs);
        spark.stop();
    }
}
