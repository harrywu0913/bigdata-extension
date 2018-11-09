package com.webex.dap.spark.core

import com.google.gson.JsonParser
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

/**
  * Created by harry on 3/5/18.
  */
object CIAPMetrics {
  val logger = LoggerFactory.getLogger(CIAPMetrics.getClass)

  val jsonParser = new JsonParser()

  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.println("Usage: CIAPMetrics <in-directoy> <out-directoy>")
      System.exit(1)
    }

    val sparkConf = new SparkConf().setAppName("CIAPMetrics")

    val sc = new SparkContext(sparkConf)

    val input = new Path(args(0));

    val dfs = FileSystem.get(sc.hadoopConfiguration)

    if (dfs.exists(input.getParent) && dfs.globStatus(input).length > 0) {

      val lines = sc.textFile(args(0))
      val metrics = lines.map { line =>
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

      metrics.saveAsHadoopFile(args(1), classOf[String], classOf[String], classOf[RDDMultipleTextOutputFormat[_, _]]);
    } else {
      logger.warn("no input files under folder => {}", args(0))
    }

    sc.stop()
  }
}