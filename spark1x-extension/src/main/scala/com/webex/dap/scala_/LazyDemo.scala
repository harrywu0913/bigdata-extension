package com.webex.dap.scala_

import scala.util.Try

object LazyDemo {

  def init(): String = {
    println("call init()")
    ""
  }

  def main(args: Array[String]): Unit = {
    val p1 = init();
    println("after p1 init()")
    println(p1)
    println(p1)


    //在声明P2时，并没有立即调用实例化方法init()，而是再使用P2的时候，才会被实例化，并且无论多次，只调用一次。
    lazy val p2 = init()
    println("after p2 init()")
    println(p2)
    println(p2)

    Try

  }
}
