package com.webex.dap.spark.streaming

import java.util.Date

object RecurringTimerDemo {
  def main(args: Array[String]): Unit = {
    val time = System.currentTimeMillis()
    val period = 1 * 60 * 1000

    println(time + " -> " + new Date(time))

    val starttime = (math.floor(time.toDouble / period) + 1).toLong * period
    println(starttime + " -> " + new Date(starttime))
  }
}
