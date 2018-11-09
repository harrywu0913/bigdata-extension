package com.webex.dap.spark.core.hbase

import org.apache.hadoop.hbase.client.{ConnectionFactory, Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.{SparkConf, SparkContext}

import scala.math.random
import scala.util.Try

/**
  * Created by harry on 1/16/18.
  */
object cSparkHBase {
  def main(args: Array[String]) {

    print("*****")
    print(System.getProperty("user.home"))
    print(System.getProperty("user.dir"))
    print("*****")

    val conf = new SparkConf()
      .setAppName("Spark HBase")


    val spark = new SparkContext(conf)

    spark.hadoopConfiguration.set(TableOutputFormat.OUTPUT_TABLE,"XX")
    val job = Job.getInstance(spark.hadoopConfiguration)
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Result])
    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])


    val slices = if (args.length > 0) args(0).toInt else 2
    val n = math.min(100000L * slices, Int.MaxValue).toInt
    // avoid overflow
    val count = spark.parallelize(1 until n, slices).map { i =>
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x * x + y * y < 1) 1 else 0
    }

    count.foreachPartition(part => {
      try {
        val hbaseconf = HBaseConfiguration.create()
        val conn = ConnectionFactory.createConnection(hbaseconf)
        val tableName = TableName.valueOf("XX")
        val table = conn.getTable(tableName)

        part.foreach(record => {
          val put = new Put(Bytes.toBytes(random * 2 - 1))
          put.addColumn(Bytes.toBytes("INFO"),Bytes.toBytes("COL"),Bytes.toBytes(record))
          Try(table.put(put)).getOrElse(table.close())
        })
        //close the table
        table.close()
        conn.close()
      } catch {
        case e: Exception =>
          e.printStackTrace()
      }

    })

    spark.stop()

    print("*****")
    print(System.getProperty("user.home"))
    print(System.getProperty("user.dir"))
    print("*****")

  }
}
