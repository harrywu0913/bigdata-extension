package com.webex.dap.utils

import java.io.{BufferedReader, FileReader}
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.{SSLContext, TrustManager, X509TrustManager}

import org.apache.http.client.config.RequestConfig
import org.apache.http.config.RegistryBuilder
import org.apache.http.conn.socket.{ConnectionSocketFactory, PlainConnectionSocketFactory}
import org.apache.http.conn.ssl.{NoopHostnameVerifier, SSLConnectionSocketFactory}
import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager


/**
  * Created by harry on 3/8/18.
  */
object HttpsClientUtils {
  def createHttpClients(): CloseableHttpClient = {
    val sslContext = SSLContext.getInstance("TLS")
    val tm = new X509TrustManager {

      override def getAcceptedIssuers = {
        val x509Certificates: Array[X509Certificate] = Array()
        x509Certificates
      }

      override def checkClientTrusted(x509Certificates: Array[X509Certificate], s: String) = {

      }

      override def checkServerTrusted(x509Certificates: Array[X509Certificate], s: String) = {

      }
    }

    sslContext.init(null, Array[TrustManager](tm), new SecureRandom());

    val poolConnManager = new PoolingHttpClientConnectionManager(RegistryBuilder.create[ConnectionSocketFactory]()
      .register("http", PlainConnectionSocketFactory.getSocketFactory())
      .register("https", new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier()))
      .build());
    poolConnManager.setMaxTotal(200)
    poolConnManager.setDefaultMaxPerRoute(20)

    HttpClients.custom()
      .setConnectionManager(poolConnManager)
      .setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(3000).build())
      .build()
  }

  def main(args: Array[String]): Unit = {

    val br = new BufferedReader(new FileReader("/Users/harry/Documents/FlumeData-rpbt1hsn007.webex.com.1528090715638.txt"));

    var line = ""
    while((line = br.readLine()) != null){
      println(line)
    }

//    val influxMsg = new StringBuilder
//    influxMsg.append("xxxxx").append(",")
//
//    val extraMetrics = new mutable.HashMap[String, String]()
//
//    extraMetrics += ("x" -> "y")
//    for ((k, v) <- extraMetrics) {
//      influxMsg.append(k).append("=").append(v).append(",")
//    }
//
//
//    val applicationId = "xxx"
//    val totalDelay = 30
//
//    influxMsg.append(s"applicationId=${applicationId}").append(" ")
//      //      .append(s"batchTime=${batchTime}").append(",")
//      //      .append(s"numRecords=${numRecords}").append(",")
//      //      .append(s"submissionTime=${submissionTime}").append(",")
//      //      .append(s"processingStartTime=${processingStartTime}").append(",")
//      //      .append(s"processingEndTime=${processingEndTime}").append(",")
//      //      .append(s"processingTime=${processingTime}").append(",")
//      //      .append(s"processingDelay=${processingDelay}").append(",")
//      //      .append(s"schedulingDelay=${schedulingDelay}").append(",")
//      .append(s"totalDelay=${totalDelay}")
//
//
//    //    httpPost.setEntity(new StringEntity(influxMsg.toString(), ContentType.APPLICATION_FORM_URLENCODED));
//    //    httpclient.execute(httpPost);
//
//    val httpclient = HttpsClientUtils.createHttpClients()
//    val httpPost = new HttpPost("http://rphf1kaf001.qa.webex.com:8086/write?db=mydb")
//    httpPost.setEntity(new StringEntity("xx,applicationId=application_1523934700130_0271 batchTime=2018-04-20T07:00:14.0Z,numRecords=1,submissionTime=2018-04-20T07:00:14.15Z,processingStartTime=2018-04-20T07:00:14.15Z,processingEndTime=2018-04-20T07:00:14.600Z,processingTime=585,processingDelay=585,schedulingDelay=0,totalDelay=585", ContentType.APPLICATION_FORM_URLENCODED));
//    val response = httpclient.execute(httpPost);
//
//    println(response)
  }
}
