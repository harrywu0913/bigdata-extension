package com.webex.dap.spark.streaming.kafka.jdbc

import java.io.{BufferedWriter, File, FileWriter}
import java.text.SimpleDateFormat
import java.util.Date

import com.google.gson.JsonParser
import com.webex.dap.utils.JDBCConnectionPool
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.slf4j.LoggerFactory

/**
  * Created by harry on 3/5/18.
  *
  * spark-submit --class com.webex.dap.spark.streaming.kafka.CIAPMetrics /opt/spark1x-extension-1.0.jar bt1-kafka-s.webex.com:9092 bt1_logstash_cmse_ciap_webex_clp 10 /spare/ciapmetrics
  */
object MetricsToMySQL {
  val logger = LoggerFactory.getLogger(MetricsToMySQL.getClass)

  val jsonParser = new JsonParser()

  def main(args: Array[String]) {
    if (args.length < 5) {
      System.err.println("Usage: MetricsToMySQL <broker> <topic> <groupid> <interval> <dest>")
      System.exit(1)
    }

    val sparkConf = new SparkConf().setAppName("MetricsToMySQL")
    // Create the context

    val Array(brokers, topics, groupid, interval, dest) = args
    //    val brokers = "rphf1kaf001.qa.webex.com:9092,rphf1kaf002.qa.webex.com:9092,rphf1kaf003.qa.webex.com:9092"
    //    val topics = "hfqa1_logstash_dap_webex_clp"
    val ssc = new StreamingContext(sparkConf, Seconds(interval.toInt))

    // Create direct kafka stream with brokers and topics
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers, "group.id" -> groupid)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)

    val metrics = messages.map(_._2)

    val sdf = new SimpleDateFormat("yyyy-MM-dd")

    metrics.foreachRDD {
      (rdd, time) => {
        if (!rdd.isEmpty()) {
          val day = sdf.format(new Date(time.milliseconds));
          rdd.foreachPartition(records => {
            println("==== 1. connection")
            lazy val connection = JDBCConnectionPool.getConnection()

            println("==== 2. connection")
            println(connection)
            println("==== 3. connection")

            connection.close()

            println("==== 4. connection")
          })
        }
      }
    }
    ssc.start()
    ssc.awaitTermination()
  }
}
