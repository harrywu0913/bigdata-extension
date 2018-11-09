package com.webex.dap.config

import com.typesafe.config.ConfigFactory

/**
  * Created by harry on 6/12/18.
  */
object ConfigDemo {
  def main(args: Array[String]): Unit = {
    val conf = ConfigFactory.load();
  }
}
