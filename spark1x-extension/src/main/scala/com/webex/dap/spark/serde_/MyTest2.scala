package com.webex.dap.spark.serde_

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by harry on 8/21/18.
  *
  * // 将map/filter等引用的成员函数，移动小的序列化的类中，避免整个类需要序列化
  */
class MyTest2(domain: String) extends Serializable {
  val list = List("a.com", "www.b.com", "a.cn", "a.com.cn", "a.org");

//  @transient
  private val sparkConf = new SparkConf().setMaster("local[*]").setAppName("AppName");
//  @transient
  private val sc = new SparkContext(sparkConf);

  val rdd = sc.parallelize(list);

  def getResult(): Array[(String)] = {
    val rootDomain = domain
    val result = rdd.filter(item => item.contains(rootDomain)).map(item => Tool1.addWWW(item))

    result.take(result.count().toInt)
  }

//  def addWWW(str: String): String = {
//    if (str.startsWith("www.")) {
//      str
//    }
//    else {
//      "www." + str
//    }
//  }
}

object Tool1{
  def addWWW(str: String): String = {
    if (str.startsWith("www.")) {
      str
    }
    else {
      "www." + str
    }
  }
}

object MyTest2 {
  def main(args: Array[String]): Unit = {
    val myTest2 = new MyTest2("www.b.com")
    myTest2.getResult()
  }
}
