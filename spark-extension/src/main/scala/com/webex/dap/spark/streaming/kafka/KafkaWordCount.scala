package com.webex.dap.spark.streaming.kafka

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by harry on 4/9/18.
  */
object KafkaWordCount {
  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.println(
        s"""
           |Usage: DirectKafkaWordCount <brokers> <topics>
           |  <brokers> is a list of one or more Kafka brokers
           |  <topics> is a list of one or more kafka topics to consume from
           |
        """.stripMargin)
      System.exit(1)
    }

    val Array(brokers, topics, zk, group, seconds, threads) = args

    // Create context with 2 second batch interval
    val sparkConf = new SparkConf().setAppName("KafkaWordCount")
    val ssc = new StreamingContext(sparkConf, Seconds(seconds.toInt))

//    ssc.addStreamingListener(new DapSparkStreamingListener)

    // Create direct kafka stream with brokers and topics
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)

    var topicsMap = Map[String, Int]()
    topicsSet.foreach(topic => {
      topicsMap += (topic -> threads.toInt)
    })


    val messages = KafkaUtils.createStream(ssc, zk, group, topicsMap, StorageLevel.MEMORY_AND_DISK)

    //    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
    //      ssc, kafkaParams, topicsSet)

    // Get the lines, split them into words, count the words and print
    val lines = messages.map(_._2)
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)

    wordCounts.print()

    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }
}

