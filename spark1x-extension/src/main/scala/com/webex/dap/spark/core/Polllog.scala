package com.webex.dap.spark.core

import org.apache.hadoop.fs.Path
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

/**
  * Created by harry on 3/5/18.
  */
object Polllog {
  val logger = LoggerFactory.getLogger(Polllog.getClass)

  //  val jsonParser = new JsonParser()

  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.println("Usage: Polllog <in-directoy> <out-directoy>")
      System.exit(1)
    }

    val sparkConf = new SparkConf().setAppName("Polllog")

    val sc = new SparkContext(sparkConf)

    val input = new Path(args(0));

    //    val dfs = FileSystem.get(sc.hadoopConfiguration)

    //    if (dfs.exists(input.getParent) && dfs.globStatus(input).length > 0) {
    val lines = sc.textFile(args(0))
    lines.filter(line => line.contains("\"featureName\": \"SessUserJoin\"") && !line.contains("_jsonparsefailure")).map { line =>
      if (line.contains("99162970958859143")) {
        ("99162970958859143", line)
      } else if (line.contains("99089653586463310")) {
        ("99089653586463310", line)
      } else if (line.contains("99154974829709584")) {
        ("99154974829709584", line)
      } else {
        ("", "")
      }
    }.filter(_._1.length > 0).repartition(200).saveAsHadoopFile(args(1), classOf[String], classOf[String], classOf[RDDMultipleTextOutputFormat[_, _]]);
    //    } else {
    //      logger.warn("no input files under folder => {}", args(0))
    //    }

    sc.stop()
  }
}