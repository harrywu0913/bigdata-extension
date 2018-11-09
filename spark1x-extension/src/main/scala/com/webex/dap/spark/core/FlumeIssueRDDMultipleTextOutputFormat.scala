package com.webex.dap.spark.core

import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat

/**
  * Created by harry on 7/13/18.
  */
class FlumeIssueRDDMultipleTextOutputFormat[K, V]() extends MultipleTextOutputFormat[K, V]() {
  override def generateFileNameForKeyValue(key: K, value: V, name: String): String = {
    (key.asInstanceOf[String].split("##",2)(0) + "/" + name)
  }

  override def generateActualKey(key: K, value: V): K = NullWritable.get().asInstanceOf[K]
}
