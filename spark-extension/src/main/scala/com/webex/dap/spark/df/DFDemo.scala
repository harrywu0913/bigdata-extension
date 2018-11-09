package com.webex.dap.spark.df

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by harry on 6/5/18.
  */
object DFDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    val sc = new SparkContext(conf)
    val sql = new SQLContext(sc)


    val df = sql.read.json("")

    val df_text = sql.read.text("")


//    sc.textFile(s"/kafka/*_logstash_telephony_hdfs/2018-07-02")
//      .filter(line => !line.contains("_jsonparsefailure"))
//      .filter(line => line.startsWith("{") && line.endsWith("}"))
//      .filter(line => (line.contains("\"Aggregator\"") || line.contains("\"VOIP\"")) && line.contains("\"QosControl\"") && line.contains("98896180823460329")
//    ).saveAsTextFile("/user/larwu/mmp.txt")


    df.show()

    sc.stop()
  }
}
