package org.apache.spark.rpc

import org.apache.spark.rpc.{RpcCallContext, RpcEndpoint, RpcEnv}

class HelloEndpoint(override val rpcEnv: RpcEnv) extends RpcEndpoint {
  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case SayHi(msg) => {
      println(s"receive $msg")
      context.reply(s"Hi, $msg")

    }
    case SayBye(msg) => {
      println(s"receive $msg")
      context.reply(s"Byte, $msg")
    }

  }

  override def onStart(): Unit = {
    println("Start Hello Endpoint")
  }

  override def onStop(): Unit = {
    println("Stop Hello Endpoint")
  }
}

case class SayHi(msg: String)

case class SayBye(msg: String)
