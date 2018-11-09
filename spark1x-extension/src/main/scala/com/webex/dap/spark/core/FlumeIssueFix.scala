package com.webex.dap.spark.core

import com.google.gson.JsonParser
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.compress.LzoCodec
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.util.Random

/**
  * Created by harry on 3/5/18.
  */
object FlumeIssueFix {
  val logger = LoggerFactory.getLogger(FlumeIssueFix.getClass)
  val jsonParser = new JsonParser()

  val random = new Random()

  def main(args: Array[String]) {
    if (args.length < 4) {
      System.err.println("Usage: FlumeIssueFix <in-directoy> <day> <out-directoy> <size> <type#featurename#metricname>")
      System.exit(1)
    }

    val sparkConf = new SparkConf().setAppName("FlumeIssueFix")
    val sc = new SparkContext(sparkConf)

    //    val dfs = FileSystem.get(sc.hadoopConfiguration)

    //    val output = new Path(args(2));
    //    if (dfs.exists(output)){
    //      dfs.delete(output,true);
    //    }

    val lines = sc.textFile(args(0))
    var metrics = lines.map { line =>
      val keyMap = new mutable.HashMap[String, String]();
      try {
        val json = jsonParser.parse(line).getAsJsonObject;
        if (json.has("type") && !json.get("type").isJsonNull) {
          keyMap.put("type", json.get("type").getAsString().toLowerCase());
        }

        if (json.has("message") && !json.get("message").isJsonNull) {
          if (json.get("message").isJsonObject) {
            val message = json.get("message").getAsJsonObject
            if (message.has("featureName") && message.get("featureName").isJsonPrimitive) {
              keyMap.put("featureName", message.get("featureName").getAsString().toLowerCase());
            }
            if (message.has("metricName") && message.get("metricName").isJsonPrimitive) {
              keyMap.put("metricName", message.get("metricName").getAsString().toLowerCase());
            }
          }
        }
      } catch {
        case e =>
      }

      (keyMap, line)
    }

    val filter_ = args(4).split("#",3);

    if (filter_(0) != null && filter_(0) != "") {
      metrics = metrics.filter(_._1.getOrElse("type", "others").equals(filter_(0)));
    }

    if (filter_(1) != null && filter_(1) != "") {
      metrics = metrics.filter(_._1.getOrElse("featureName", "others").equals(filter_(1)));
    }

    if (filter_(2) != null && filter_(2) != "") {
      metrics = metrics.filter(_._1.getOrElse("metricName", "others").equals(filter_(2)));
    }

    val records = metrics.map(record => {
      (new StringBuilder().append(record._1.getOrElse("type", "others")).append("/").append(record._1.getOrElse("featureName", "others")).append("-").append(record._1.getOrElse("metricName", "others")).append("/day=").append(args(1)).append("##").append(random.nextInt(args(3).toInt)).toString(), record._2)
    }).reduceByKey((msg_1, msg_2) => msg_1 + "\n" + msg_2)

    records.saveAsHadoopFile(args(2), classOf[String], classOf[String], classOf[FlumeIssueRDDMultipleTextOutputFormat[_, _]], classOf[LzoCodec]);

    sc.stop()
  }
}