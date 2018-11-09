package com.webex.dap.spark.hbase

import org.apache.hadoop.hbase.client.{ConnectionFactory, HTable, Put}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by harry on 6/20/18.
  */
object SparkToHBase_1 {
  def main(args: Array[String]): Unit = {
//    if (args.length < 3) {
//      println("Usage: [input] [zk] [table]")
//      System.exit(1);
//    }
//
//    val (input, zk, table:String) = Array(args)
//
//    val sc = new SparkContext(new SparkConf() /*.setMaster("local[*]")*/ .setAppName("SparkToHBase_3"))
//
//    val rdd = sc.textFile(input)
//    val data = rdd.map(_.split(",")).map(x => (x(0) + x(1), x(2)))
//
//    data.foreachPartition(records => {
//      println("1.=====")
//      val conf = HBaseConfiguration.create();
//      conf.set("hbase.zookeeper.quorum", zk)
//      val htable = ConnectionFactory.createConnection(conf).getTable(TableName.valueOf(table)).asInstanceOf[HTable]
//      htable.setAutoFlush(false, false)
//      println("2.=====")
//      records.foreach { record =>
//        println("4.1. =====")
//        val put = new Put(Bytes.toBytes(record._1))
//        put.addColumn(Bytes.toBytes("info"),Bytes.toBytes("c1"),Bytes.toBytes(record._2))
//        htable.put(put)
//        println("4.2. =====")
//      }
//
//      println("3.=====")
//      htable.flushCommits()
//      println("5.=====")
//    })
//
//    println("6.=====")
//    sc.stop()
//    println("7.=====")
  }
}
