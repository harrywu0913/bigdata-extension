package com.webex.dap.spark.streaming.kafka

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by harry on 4/9/18.
  */
object DirectKafkaWordCount {
  def main(args: Array[String]) {
    //    if (args.length < 2) {
    //      System.err.println(
    //        s"""
    //           |Usage: DirectKafkaWordCount <brokers> <topics>
    //           |  <brokers> is a list of one or more Kafka brokers
    //           |  <topics> is a list of one or more kafka topics to consume from
    //           |
    //        """.stripMargin)
    //      System.exit(1)
    //    }

    val Array(brokers, topics) = args
    //    val brokers = "rphf1kaf001.qa.webex.com:9092,rphf1kaf002.qa.webex.com:9092,rphf1kaf003.qa.webex.com:9092"
    //    val topics = "hfqa1_logstash_dap_webex_clp"

    // Create context with 2 second batch interval
    val sparkConf = new SparkConf().setAppName("DirectKafkaWordCount")
    //      .setMaster("local[*]").set("spark.broadcast.compress", "false")
    val ssc = new StreamingContext(sparkConf, Seconds(10))

    //    ssc.addStreamingListener(new DapSparkStreamingListener)

    // Create direct kafka stream with brokers and topics
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, topicsSet)

    // Get the lines, split them into words, count the words and print
    val lines = messages.map(_._2)
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)
    wordCounts.print()


    lines.foreachRDD(line => {
      line.foreachPartition(records => {
        //
        //
        //
      })
    })


    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }
}

