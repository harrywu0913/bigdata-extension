package com.webex.dap.spark.df

import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.Try

/**
  * Created by harry on 6/5/18.
  */
object DFDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("dfdemo")
    conf.set("spark.broadcast.compress", "false")
    val sc = new SparkContext(conf)
    val sql = new SQLContext(sc)

    import sql.implicits._

    /*
        sql.udf.register[RT,A1](name: String,func: Function1[A1,RT]):UserDefinedFunction
     */
    sql.udf.register[Int, String]("strlen", (s: String) => s.length)

    /*
      sql.udf.register[RT,A1,A2](name: String,func: Function1[A1,A2,RT]):UserDefinedFunction
     */
    sql.udf.register[Int, Int, Int]("max", (a: Int, b: Int) => math.max(a, b))

    sql.udf.register("myudfs", new MyAvgUDAF)

    val df = sql.read.json("/Users/harry/Documents/software/spark-1.6.0-bin-hadoop2.6/data/df/json.data")

    df.agg(max(""));

    df.groupBy($"message");

    df.show()

    sc.stop()
  }

  class MyAvgUDAF extends UserDefinedAggregateFunction {
    /*
      需要指定具体的输入数据的类型.

      name 可以是随意的.
     */
    override def inputSchema: StructType = StructType(Array(StructField("input_1", StringType, true)))

    /*
      在进行聚合操作时所需要处理的数据中间结果类型
     */
    override def bufferSchema: StructType = StructType(Array(StructField("input_1", IntegerType, true), StructField("input_2", IntegerType, true)))

    /*
      返回类型
     */
    override def dataType: DataType = IntegerType

    override def deterministic: Boolean = false

    /*
      初始化
     */
    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer.update(0, 0)
      buffer.update(0, 1)
    }

    /*
      在进行聚合时候，每当有新的值进来，对分组后的聚合如何进行计算。即本地聚合操作

      用输入的行row来更新buffer
     */
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      buffer.update(0, buffer.getInt(0) + 1)
      buffer.update(1, buffer.getInt(0) + Integer.valueOf(input.getString(0)))
    }

    /*
      最后分布式节点进行local，reduce后，进行全局的merge操作
     */
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1.update(0, buffer1.getInt(0) + buffer2.getInt(0))
      buffer1.update(1, buffer1.getInt(1) + buffer2.getInt(1))
    }

    /*
      返回UDAF最后的计算结果
     */
    override def evaluate(buffer: Row): Any = {
      buffer.getInt(1) / buffer.getInt(0)
    }
  }

}
