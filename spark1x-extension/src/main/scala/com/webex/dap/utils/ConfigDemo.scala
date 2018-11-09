package com.webex.dap.utils

import com.typesafe.config.ConfigFactory

/**
  * Created by harry on 9/27/18.
  */
object ConfigDemo {
  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.load();

    println(conf.getString("a.a1"))
    println(conf.getString("b.b1"))
    println(conf.getString("c.c1"))
  }
}
