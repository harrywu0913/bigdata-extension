package com.webex.dap.spark.listener

import java.text.SimpleDateFormat
import java.util.{Date, Properties, TimeZone}

import com.google.gson.JsonObject
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.{Logging, SparkEnv}
import org.apache.spark.scheduler.{SparkListener, SparkListenerApplicationEnd}
import org.apache.spark.streaming.scheduler.{StreamingListener, StreamingListenerBatchCompleted}

import scala.collection.mutable


/**
  * Created by harry on 4/8/18.
  */
class DapSparkStreamingListener extends StreamingListener with SparkListener with Logging {

  @volatile var valid: Boolean = true
  @volatile var stopped: Boolean = false

  var format: SimpleDateFormat = null
  val props = new Properties()

  @volatile var kafkaProducer: KafkaProducer[String, String] = null
//  @volatile var httpclient: CloseableHttpClient = null

  //  var eventQueue: LinkedBlockingQueue[String] = null

  val conf = SparkEnv.get.conf
  val applicationId = conf.get("spark.app.id", "")

  val brokers = conf.get("spark.dap.monitor.kafka.brokers", "")
  val topic = conf.get("spark.dap.monitor.kafka.topic", "")
  val metrics_store = conf.get("spark.dap.monitor.metrics.store", "cmp")

  // properties for clp
  val clpExtraKV = conf.get("spark.dap.monitor.clp.extra.metrics", "component=dap,eventtype=metrics")

  // properties for cmp
  val cmpMessageType = conf.get("spark.dap.monitor.cmp.msg.type", "streamjobmonitor")

  if (brokers == null || brokers.trim.length == 0) {
    valid = false
  }

  if (topic == null || topic.trim.length == 0) {
    valid = false
  }

  if (valid) {
    props.put("client.id", (s"dapstreaming-monitor-$brokers-$topic").replace(':', '-').replace(',', '-'))
    props.put("bootstrap.servers", brokers)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    //    eventQueue = new LinkedBlockingQueue[String]()

    format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'")
    format.setTimeZone(TimeZone.getTimeZone("GMT"))

    logInfo("DapSparkStreamingListener.init.kafka.properties")
  }

  def getKafkaProducer(): KafkaProducer[String, String] = {
    if (kafkaProducer == null) {
      synchronized {
        if (kafkaProducer == null) {
          kafkaProducer = new KafkaProducer[String, String](props);
          logInfo("DapSparkStreamingListener.getKafkaProducer() end")
        }
      }
    }
    kafkaProducer
  }

//  def getHttpClient(): CloseableHttpClient = {
//    if (httpclient == null) {
//      synchronized {
//        if (httpclient == null) {
//          httpclient = HttpsClientUtils.createHttpClients()
//        }
//      }
//    }
//    httpclient
//  }


  //  override def onReceiverStarted(receiverStarted: StreamingListenerReceiverStarted): Unit = {
  //    if (valid) {
  //      logInfo("DapSparkStreamingListener.onReceiverStarted start")
  //
  //      stopped = false
  //
  //
  //      //      new Thread("DapSparkStreamingListenerThread") {
  //      //        setDaemon(true)
  //      //
  //      //
  //      //        override def run(): Unit = {
  //      //          logInfo("DapSparkStreamingListener Startup")
  //      //
  //      //          while (!stopped && !Thread.currentThread().isInterrupted) {
  //      //            try {
  //      //              val msg = eventQueue.take()
  //      //              logInfo(s"DapSparkStreamingListener take message from queue: ${msg}")
  //      //              if (msg != null) {
  //      //                getKafkaProducer().send(new ProducerRecord(topic, msg))
  //      //                logInfo(s"DapSparkStreamingListener sent message to kafka: ${msg}")
  //      //              }
  //      //            } catch {
  //      //              case t: Throwable =>
  //      //                logWarning("error:run() " + t)
  //      //            }
  //      //          }
  //      //
  //      //          logInfo("==========================================")
  //      //          logInfo("DapSparkStreamingListener Thread Finished")
  //      //        }
  //      //      }.start()
  //
  //      //      executors = Executors.newSingleThreadExecutor();
  //      //      //
  //      //      executors.submit(new Runnable {
  //      //        override def run() = {
  //      //          logInfo("DapSparkStreamingListener.onReceiverStarted start")
  //      //          while (!stopped && !Thread.currentThread().isInterrupted) {
  //      //            try {
  //      //              val msg = eventQueue.take()
  //      //              logInfo(s"DapSparkStreamingListener take message from queue: ${msg}")
  //      //              if (msg != null) {
  //      //                getKafkaProducer().send(new ProducerRecord(topic, msg))
  //      //                logInfo(s"DapSparkStreamingListener sent message to kafka: ${msg}")
  //      //              }
  //      //            } catch {
  //      //              case t: Throwable =>
  //      //                logWarning("error:run() " + t)
  //      //            }
  //      //          }
  //      //        }
  //      //      })
  //
  //      logInfo("DapSparkStreamingListener.onReceiverStarted end")
  //    }
  //  }

  //  override def onReceiverStopped(receiverStopped: StreamingListenerReceiverStopped): Unit = {
  //    if (valid) {
  //      //      logInfo("DapSparkStreamingListener.onReceiverStopped start")
  //      //
  //      //      stopped = true
  //      //
  //      //      if (eventQueue != null) {
  //      //        var msg: String = null
  //      //        while (!eventQueue.isEmpty) {
  //      //          msg = eventQueue.poll(1, TimeUnit.SECONDS)
  //      //          if (msg != null) {
  //      //            getKafkaProducer().send(new ProducerRecord(topic, msg))
  //      //            logInfo(s"DapSparkStreamingListener.onReceiverStopped sent message to kafka: ${msg}")
  //      //          }
  //      //        }
  //      //      }
  //      //
  //      //      if (kafkaProducer != null) {
  //      //        kafkaProducer.close()
  //      //        kafkaProducer = null
  //      //      }
  //
  //      logInfo("DapSparkStreamingListener.onReceiverStopped stop")
  //    }
  //  }

  override def onBatchCompleted(batchCompleted: StreamingListenerBatchCompleted): Unit = {
    if (valid) {
      try {
        logInfo("onBatchCompleted start====>")

        composeMetrics(batchCompleted) match {
          case Some(metrics) =>
            logInfo(s"onBatchCompleted:${metrics}")
            getKafkaProducer().send(new ProducerRecord(topic, metrics))
          case _ =>
          //Ignore
        }
      } catch {
        case t: Throwable =>
          logWarning("error:onBatchCompleted() " + t)
      }
    }
  }

  def composeMetrics(batchCompleted: StreamingListenerBatchCompleted): Option[String] = {

    val batchInfo = batchCompleted.batchInfo

    val metrics = new JsonObject
    val message = new JsonObject

    val numRecords = batchInfo.numRecords
    val processingTime = (batchInfo.processingEndTime.getOrElse(0L) - batchInfo.processingStartTime.getOrElse(0L))
    val processingDelay = batchInfo.processingDelay.getOrElse(0L)
    val schedulingDelay = batchInfo.schedulingDelay.getOrElse(0L)
    val totalDelay = batchInfo.totalDelay.getOrElse(0L)

    message.addProperty("applicationId", applicationId)
    message.addProperty("batchTime", format.format(new Date(batchInfo.batchTime.milliseconds)))
    message.addProperty("numRecords", numRecords)
    message.addProperty("submissionTime", format.format(new Date(batchInfo.submissionTime)))

    if (batchInfo.processingStartTime.isDefined) {
      message.addProperty("processingStartTime", format.format(new Date(batchInfo.processingStartTime.get)))
    }

    if (batchInfo.processingEndTime.isDefined) {
      message.addProperty("processingEndTime", format.format(new Date(batchInfo.processingEndTime.get)))
    }

    message.addProperty("processingTime", processingTime)
    message.addProperty("processingDelay", processingDelay)
    message.addProperty("schedulingDelay", schedulingDelay)
    message.addProperty("totalDelay", totalDelay)

    extraMetrics() match {
      case Some(extraMetrics) =>
        for ((k, v) <- extraMetrics) {
          message.addProperty(k, v)
        }
      case _ =>
      //Ignore
    }

    if ("clp".equals(metrics_store)) {

      metrics.add("message", message)

      if (clpExtraKV.length > 0) {
        clpExtraKV.split(",").map(kv => kv.split("=")).map(x => {
          metrics.addProperty(x(0), x(1))
        })
      }
    } else if ("cmp".equals(metrics_store)) {
      metrics.addProperty("type", cmpMessageType)
      metrics.addProperty("objectString", message.toString)
      metrics.addProperty("timestamp", message.get("batchTime").getAsString)
    }

    Some(metrics.toString)
  }

  def extraMetrics(): Option[mutable.Map[String, String]] = {
    None
  }

  override def onApplicationEnd(applicationEnd: SparkListenerApplicationEnd): Unit = {
    if (kafkaProducer != null) {
      kafkaProducer.close()
      kafkaProducer = null
    }

//    if (httpclient != null) {
//      httpclient.close()
//      httpclient = null
//    }

    //    if (executors != null) {
    //      executors.shutdown()
    //          executors = null
    //    }
  }
}
