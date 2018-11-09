package com.webex.dap.spark.core

import org.apache.spark.{SparkConf, SparkContext}

import scala.math.random

/**
  * Created by harry on 9/13/18.
  */
object SparkWordCountDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Spark WordCount").setMaster("local[*]")
    conf.set("spark.broadcast.compress", "false")
    //    conf.set("spark.default.parallelism","8");

    val sc = new SparkContext(conf)

    val rdd = sc.textFile("file:///Users/harry/Documents/project/cmse/dap-extension-self/README.txt")
      .flatMap(_.split(" "))
      .filter(_.size > 0)
      .map(w => (w, 1))
      .reduceByKey((a, b) => a + b)
      .filter(_._2 >= 2)


    rdd.count()

    //    rdd.foreach(record => {
    //      println(record)
    //    })
    //
    //    rdd.foreachPartition(records => {
    //
    //      records.foreach(record => {
    //
    //      })
    //    })

    System.in.read()

    sc.stop()
  }
}
