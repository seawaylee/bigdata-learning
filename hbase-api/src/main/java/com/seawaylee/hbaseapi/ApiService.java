package com.seawaylee.hbaseapi;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * @author SeawayLee
 * @create 2018/8/14 00:00
 */
public class ApiService {
    private static Connection connection;
    private static Admin admin;

    public static void createConnections() throws IOException {
        // 创建连接
        connection = ConnectionFactory.createConnection();
        admin = connection.getAdmin();
    }

    public static void closeConnection() throws IOException {
        // 关闭资源
        admin.close();
        connection.close();
    }

    /**
     * 创建表
     *
     * @throws IOException
     */
    public static void createOrOverwriteTable() throws IOException {
        // 删除表
        HTableDescriptor table = new HTableDescriptor(TableName.valueOf("UCF"));
        if (admin.tableExists(table.getTableName())) {
            admin.disableTable(table.getTableName());
            admin.deleteTable(table.getTableName());
        }
        // 构造列族
        HColumnDescriptor cf = new HColumnDescriptor("cf1");
        cf.setMaxVersions(5);
        table.addFamily(cf);
        // 执行命令 - 重建表
        admin.createTable(table);
        // 额外添加一个列族
        admin.addColumn(table.getTableName(), new HColumnDescriptor("cf2"));
    }

    /**
     * 插入数据
     *
     * @param table
     * @throws IOException
     */
    public static void putData(Table table) throws IOException {
        Put row = new Put(Bytes.toBytes("row1"));
        row.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("name"), System.currentTimeMillis(), Bytes.toBytes("compare"))
                .addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("age"), System.currentTimeMillis(), Bytes.toBytes(15))
                .addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("addr"), System.currentTimeMillis(), Bytes.toBytes("北京"));
        // 直接存储
        //table.put(row);
        // 原子操作，判定更新前后的值相等才put
        //System.out.println(table.checkAndPut(Bytes.toBytes("row1"), Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes("asdasdasds"), row)); //
        // 判断这列是否存在，不存在则put
        //System.out.println(table.checkAndPut(Bytes.toBytes("row1"), Bytes.toBytes("cf1"), Bytes.toBytes("namesss"), null, row));
        // 比较大小关系后put
        //System.out.println(table.checkAndPut(Bytes.toBytes("row1"), Bytes.toBytes("cf1"), Bytes.toBytes("age"), CompareFilter.CompareOp.LESS, Bytes.toBytes(14), row));
        // 判断是否存在
        System.out.println(row.has(Bytes.toBytes("cf1"), Bytes.toBytes("age"), Bytes.toBytes(15)));

    }

    public static void main(String[] args) throws IOException {
        createConnections();
        //createOrOverwriteTable();
        Table table = connection.getTable(TableName.valueOf("UCF"));
        putData(table);
        closeConnection();
    }
}
