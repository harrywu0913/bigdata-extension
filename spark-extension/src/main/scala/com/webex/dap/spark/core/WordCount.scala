package com.webex.dap.spark.core

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by harry on 7/10/18.
  */
object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("wordcount").setMaster("local")

    val sc = new SparkContext(conf)

    val file = sc.textFile("file:///Users/harry/Documents/flume.conf")
//    val rdd_1 = file.flatMap(_.split(" "))
//    val rdd_2 = rdd_1.map((_, 1)).cache()
//    val rdd_3 = rdd_2.reduceByKey(_ + _)

    println(file.count())

    sc.stop()
  }
}
