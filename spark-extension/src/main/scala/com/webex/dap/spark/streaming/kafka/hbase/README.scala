package com.webex.dap.spark.streaming.kafka.hbase

/**
  * Created by harry on 4/25/18.
  *
  * https://blog.csdn.net/gpwner/article/details/73530134
  *
  * Spark 写HBase的三种方法对比
  *   spark-streaming_2.11
  *   hbase-client
  *   hbase-server
  *
  * way1:
  *   每条写进一条，调用API
  *     void put(Put put) throws IOException
  *
  *
  *     val rdd =
  *
  *     rdd.foreachPartition(record => {
  *       val hbaseConf = HBaseConfiguration.create()
  *       hbaseConf.set("","")
  *       hbaseConf.set("","")
  *       hbaseConf.set("","")
  *       ...
  *       ...
  *
  *       val conn = ConnectionFactory.createConnection(hbaseConf)
  *       val table = conn.getTable(TableName.valueOf(""))
  *
  *       record.foreach(value => {
  *         var put = new Put(Bytes.toBytes(""))
  *         put.addColumn(Bytes.toBytes(""),Bytes.toBytes(""),Bytes.toBytes(""))
  *
  *         table.put(put)
  *       })
  *
  *       table.close()
  *       conn.close()
  *     })
  *
  * way2:
  *   批量写入HBase
  *     void put(final List<Put> puts)
  *
  * way3:
  *
  */
object README {

}
