package com.webex.dap.spark.streaming.kafka.hbase

import com.google.gson.JsonParser
import kafka.serializer.StringDecoder
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat
import org.apache.hadoop.mapreduce.Job
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

/**
  * Created by harry on 4/25/18.
  */
object StreamingToHBase2 {
  val jsonParser = new JsonParser();

  val logger = LoggerFactory.getLogger(StreamingToHBase.getClass)

  def main(args: Array[String]): Unit = {
    val Array(name, brokers, group, topics, seconds) = args

    val topicSet = topics.split(",").toSet
    val kafkaParams = Map[String, String](ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers, ConsumerConfig.GROUP_ID_CONFIG -> group)

    val sc = new SparkContext(new SparkConf().setAppName(name) /*.setMaster("local[*]")*/)
    val ssc = new StreamingContext(sc, Seconds(seconds.toInt))
    val hiveContext = new HiveContext(sc)

    val lines = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicSet)

    sc.hadoopConfiguration.set(TableOutputFormat.OUTPUT_TABLE,"monitor_metrics")
    var job = Job.getInstance(sc.hadoopConfiguration)
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Result])
    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])

    lines.foreachRDD(rdd => {
      if (rdd.partitions.length > 0) {
        val puts = rdd
          .map(_._2)
          .map(jsonParser.parse(_).getAsJsonObject)
          .filter(line => (line.has("messagetype") && "streaming_job_monitor".equals(line.get("messagetype").getAsString)))
          .map(line => {
            val message = line.get("message").getAsJsonObject
            val put = new Put(message.get("timestamp").getAsString.getBytes)
            put.addColumn("INFO".getBytes, "name".getBytes, message.get("name").getAsString.getBytes)
            put.addColumn("INFO".getBytes, "sourceTopic".getBytes, message.get("sourceTopic").getAsString.getBytes)
            put.addColumn("INFO".getBytes, "processedRowsPerSecond".getBytes, message.get("processedRowsPerSecond").getAsString.getBytes)
            put.addColumn("INFO".getBytes, "inputRowsPerSecond".getBytes, message.get("inputRowsPerSecond").getAsString.getBytes)
            put.addColumn("INFO".getBytes, "Lag".getBytes, message.get("Lag").getAsString.getBytes)
            (new ImmutableBytesWritable,put)
          }).saveAsNewAPIHadoopDataset(job.getConfiguration)

        logger.info("StreamingToHBase.foreachPartition end(): ")
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
