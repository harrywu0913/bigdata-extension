package com.webex.dap.spark.serde_

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by harry on 8/21/18.
  *
  * // 因为getResult引用了类的成员变量domain，那么类MyTest1必须需要序列化。 而。sparkconf/sc不能序列化，所以有异常 Task not serializable
  *
  * // 1. 使用 @transient定义 sparkconf/sc
  * // 2. 使用局部变量 如 val rootDomain = domain.
  */
class MyTest1(domain: String) extends Serializable {
  val list = List("a.com", "www.b.com", "a.cn", "a.com.cn", "a.org");

//  @transient
  private val sparkConf = new SparkConf().setMaster("local[*]").setAppName("AppName");
//  @transient
  private val sc = new SparkContext(sparkConf);

  val rdd = sc.parallelize(list);

  def getResult(): Array[(String)] = {

    val rootDomain = domain
    val result = rdd.filter(item => item.contains(rootDomain))

//    val result = rdd.filter(item => item.contains(domain))

    result.take(result.count().toInt)
  }
}

object MyTest1{
  def main(args: Array[String]): Unit = {
    val myTest1 = new MyTest1("www.b.com")
    myTest1.getResult()
  }
}
