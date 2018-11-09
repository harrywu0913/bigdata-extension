package com.webex.dap.spark.streaming.kafka

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
object CIAPMetrics {
  val logger = LoggerFactory.getLogger(CIAPMetrics.getClass)

  val jsonParser = new JsonParser()

  def main(args: Array[String]) {
    if (args.length < 5) {
      System.err.println("Usage: CIAPMetrics <broker> <topic> <groupid> <interval> <dest>")
      System.exit(1)
    }

    val sparkConf = new SparkConf().setAppName("CIAPMetrics")
    // Create the context

    val Array(brokers, topics, groupid, interval, dest) = args
    //    val brokers = "rphf1kaf001.qa.webex.com:9092,rphf1kaf002.qa.webex.com:9092,rphf1kaf003.qa.webex.com:9092"
    //    val topics = "hfqa1_logstash_dap_webex_clp"
    val ssc = new StreamingContext(sparkConf, Seconds(interval.toInt))

    // Create direct kafka stream with brokers and topics
    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers, "group.id" -> groupid)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)

    val metrics = messages.map(_._2).map { line =>
      var key = ""
      var value = ""
      try {
        val json = jsonParser.parse(line).getAsJsonObject;
        if (json.has("metrics") && !json.get("metrics").isJsonNull) {
          val metrics = json.get("metrics").getAsJsonObject
          if (metrics.has("device") && !metrics.get("device").isJsonNull) {
            key = metrics.get("device").getAsString
            value = line
          }
        }
      } catch {
        case e =>
          e.printStackTrace()
          logger.error("lines.map.error. line: => " + line + ",e=> {}", e)
      }
      (key, value)
    }.filter(k => k._2.length != 0).reduceByKey((msg_1, msg_2) => msg_1 + "\n" + msg_2)

    val sdf = new SimpleDateFormat("yyyy-MM-dd")

    metrics.foreachRDD {
      (rdd, time) => {
        if (!rdd.isEmpty()) {
          val day = sdf.format(new Date(time.milliseconds));
          //          var bws = scala.collection.mutable.Map[String, BufferedWriter]();
          val it = rdd.toLocalIterator

          lazy val connection = JDBCConnectionPool.getConnection()


          while (it.hasNext) {
            val record = it.next().asInstanceOf[Tuple2[String, String]];
            val key = record._1.asInstanceOf[String];
            val values = record._2.asInstanceOf[String];

            var bw: BufferedWriter = null;
            //            if (!bws.contains(s"$day-$key")) {
            val file = new File(s"$dest/$day/$key.csv")

            if (!file.getParentFile.exists) {
              file.getParentFile.mkdirs;
              file.getParentFile.setReadable(true, false);
              file.getParentFile.setWritable(true, false);
              file.getParentFile.setExecutable(true, false);
            }

            if (!file.exists()) {
              file.createNewFile()
              file.setReadable(true, false);
              file.setWritable(true, false);
              file.setExecutable(true, false);
            }
            bw = new BufferedWriter(new FileWriter(file, true))
            //              bws.put(s"$day-$key", bw);
            //            }

            bw.write(values + "\n")

            bw.flush()
            bw.close()
          }

          //          if (!bws.isEmpty) {
          //            bws.foreach {
          //              case (key, bw) => {
          //                bw.flush()
          //                bw.close()
          //              }
          //            }
          //
          //            bws.clear()
          //            bws = null
          //          }
        }
      }
    }
    ssc.start()
    ssc.awaitTermination()
  }
}
