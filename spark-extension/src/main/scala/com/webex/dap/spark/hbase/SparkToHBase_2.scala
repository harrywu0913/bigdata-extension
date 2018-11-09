package com.webex.dap.spark.hbase

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by harry on 6/20/18.
  */
object SparkToHBase_2 {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("SparkToHBase_3"))
    val conf = HBaseConfiguration.create()

    val job = Job.getInstance(conf)
    job.getConfiguration.set("hbase.zookeeper.quorum","")
    job.getConfiguration.set(TableOutputFormat.OUTPUT_TABLE,"")
    job.setOutputFormatClass(classOf[TableOutputFormat[_]])



  }
}
