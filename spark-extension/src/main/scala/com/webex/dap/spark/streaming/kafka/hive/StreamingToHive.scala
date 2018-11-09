package com.webex.dap.spark.streaming.kafka.hive

import com.google.gson.JsonParser
import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.sql.Row
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.types._
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by harry on 4/25/18.
  */
object StreamingToHive {
  val jsonParser = new JsonParser();

  def main(args: Array[String]): Unit = {
    val Array(name, brokers, group, topics,seconds) = args

    //    val name = "monitor"
    //    val brokers = "bt1-kafka-s.webex.com:9092"
    //    val group = "monitor_xx"
    //    val topics = "bt1_logstash_dap_webex_clp"

    val topicSet = topics.split(",").toSet
    val kafkaParams = Map[String, String](ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers, ConsumerConfig.GROUP_ID_CONFIG -> group)
    //    val km = new KafkaMana

    val sc = new SparkContext(new SparkConf().setAppName(name) /*.setMaster("local[*]")*/)
    val ssc = new StreamingContext(sc, Seconds(seconds.toInt))
    val hiveContext = new HiveContext(sc)

    val lines = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicSet)

    val structType = StructType(Array(
      StructField("name", StringType, true),
      StructField("s", StringType, true),
      StructField("processed_rows_per_second", LongType, true),
      StructField("input_rows_per_second", LongType, true),
      StructField("lag", LongType, true),
      StructField("timestamp", StringType, true)
    ))

    lines.foreachRDD(rdd => {
      if (rdd.partitions.length > 0) {
        //      if (!rdd.isEmpty()) {
        val words = rdd
          .map(_._2)
          .map(jsonParser.parse(_).getAsJsonObject)
          .filter(line => (line.has("messagetype") && "streaming_job_monitor".equals(line.get("messagetype").getAsString)))
          .map(line => {
            val message = line.get("message").getAsJsonObject
//            val result = new StringBuilder
            //            val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH-mm-ss.S'Z'")
            Row(message.get("name").getAsString,
              message.get("sourceTopic").getAsString,
              message.get("processedRowsPerSecond").getAsLong,
              message.get("inputRowsPerSecond").getAsLong,
              message.get("Lag").getAsLong,
              message.get("timestamp").getAsString)


            val result = new StringBuilder
            result.append(message.get("name").getAsString).append(",")
              .append(message.get("sourceTopic").getAsString).append(",")
              .append(message.get("processedRowsPerSecond").getAsLong).append(",")
              .append(message.get("inputRowsPerSecond").getAsLong).append(",")
              .append(message.get("Lag").getAsLong).append(",")
              .append(message.get("timestamp").getAsString)

            result.toString()
          })

        if(!words.isEmpty()){
          words.saveAsTextFile("/tmp/")
        }
        //        if (!words.isEmpty()) {
        //        val hiveContext = new HiveContext(rdd.context)
//        val monitorMetricsDF = hiveContext.createDataFrame(words, structType)
//        monitorMetricsDF.write.mode(SaveMode.Append).insertInto("default.monitor_metrics_2")
        //        }
        //        words.collect().foreach(println)
        //      }
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
