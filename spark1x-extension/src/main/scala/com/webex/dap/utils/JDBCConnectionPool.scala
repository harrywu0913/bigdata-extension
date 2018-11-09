package com.webex.dap.utils

import java.sql.{Connection, DriverManager}

import org.slf4j.LoggerFactory

object JDBCConnectionPool {
  val logger = LoggerFactory.getLogger(this.getClass)

  def getConnection(): Connection = {
    Class.forName("org.jdbc.mysql.Driver")
    return DriverManager.getConnection("","","")
  }
}
