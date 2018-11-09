package com.webex.dap.spark.core

import scala.math.random
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by harry on 9/13/18.
  */
object SparkPiDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Spark Pi").setMaster("local[*]")
    val sc = new SparkContext(conf)

    //    sc.broadcast(20)

    val slices = if (args.length > 0) args(0).toInt else 2
    val n = math.min(100000L * slices, Int.MaxValue).toInt

    var rawrdd = sc.parallelize(1 until n, slices)
    // avoid overflow
    val count = rawrdd.map { i =>
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x * x + y * y < 1) 1 else 0
    }.cache().reduce(_ + _)

    println("Pi is roughly " + 4.0 * count / (n - 1))


    /*
      map
     */

    rawrdd.map(i => {
    })
    rawrdd.map { i =>
    }

    /*
      filter
     */

    rawrdd.filter(i => {
      false
    })
    rawrdd.filter { i =>
      false
    }

    /*
      flatMap
     */
    //    rawrdd.flatMap({ i =>
    //      new Array(i)
    //    })


    /*
      groupBy
     */
    val groupByRDD = rawrdd.groupBy { x =>
      if (x % 2 == 0) {
        "even"
      } else {
        "odd"
      }
    }

    /*
      groupBy = map + groupByKey
     */
    rawrdd.map(x => {
      val key = if (x % 2 == 0) {
        "even"
      } else {
        "odd"
      }

      (key, x)
    }).groupByKey()

    /*
    <====><====><====>
      combineByKey 是Spark中一个比较核心的高阶函数，其他一些高阶键值对函数底层都是用它实现的。诸如groupByKey/reduceByKey
     */


    /*
    def combineByKeyWithClassTag[C](
      createCombiner: V => C,
      mergeValue: (C, V) => C,
      mergeCombiners: (C, C) => C,
      partitioner: Partitioner,
      mapSideCombine: Boolean = true,
      serializer: Serializer = null)(implicit ct: ClassTag[C]): RDD[(K, C)] = {
      }


        createCombiner: combineByKey() 会遍历分区中所有的元素，因此每个元素的key，要么还没有遇到，要么就和之前的某个元素的key相同。如果是一个新的元素，combineByKey就会使用一个createCombiner()函数来创建那个key对应的累加器的初始值

        mergeValue: 如果这是一个处理当前分区之前已经遇到的key，它会使用mergeValue方法把该key的累加器对应的当前值，与这个新的值进行合并

        mergeCombiners: 由于每个分区都是独立处理的，因此对于同一个key可以由多个累加器，如果有两个或是更多分区都是对应同一个key的累加器，就需要使用用户提供的mergeCombiners()方法将各个分区的结果进行合并
     */

    //example1。 计算学生的平均成绩

    val initialScores = Array(("Fred", 88.0), ("Fred", 95.0), ("Fred", 91.0), ("Wilma", 93.0), ("Wilma", 95.0), ("Wilma", 98.0))

    val wilamAndFredScores = sc.parallelize(initialScores).cache()

    val createScoreCombiner = (score: Double) => (1, score)

    val mergeScoreValue = (collector: (Int, Double), score: Double) => {
      (collector._1 + 1, collector._2 + score)
    }

    val mergeScoreCombiner = (collectorA: (Int, Double), collectorB: (Int, Double)) => {
      (collectorA._1 + collectorB._1, collectorA._2 + collectorB._2)
    }

    val scores = wilamAndFredScores.combineByKey(createScoreCombiner, mergeScoreValue, mergeScoreCombiner);

    scores.collectAsMap().map(record => {
      (record._1, record._2._2 / record._2._1)
    }).foreach(print)

    /*
      aggregate[U](zeroValue: U)(seqOp:(U,T)=>U,combOp:(U,U)=>U):U

        seqOp操作会聚合各个分区中的元素，然后combOp操作会把所有分区的聚合结果再聚合。两个操作的初始值都是zeroValue。
        seqOp操作会遍历分区中所有的元素T，第一个T和zerovalue做操作，结果在作为与第二个T做操作的zerovalue。直到遍历完这个分区。
        combOp操作就是把各个分区的结果，再聚合。

        examples: List(1,2,3,4,5,6,7,9,10) 对List求平均值。


     */

    val rdd = List(1, 2, 3, 4, 5, 6, 7, 9, 10)

    val zerovalue = (0, 0)

    // number即每个分区的每个元素，即1，2，3....。
    /*
      即seqOp的操作如下：
        (0,0),1 => (0+1,0+1) => (1,1)

        (1,1),2 => (1+2,1+1) => (3,2)

        (3,2),3 => (3+3,2+1) => (6,3)

          ..
            ..
              ..
        (45,9)

      这个是单线程模式下，

      如在分布式下，可能存在很多分区。即每个分区作用了seqOp之后。
      (10,4) (26,4) (9,1) 三个分区数据集

      最后三个分区结果，再作用combOp

      (10,4),(26,4) => (10+26,4+4) => (36,8)
      (36,8),(9,1)  => (36+9,8+1)  => (45,9)
     */
    val seqOp = (zerovalue: (Int, Int), number: Int) => {
      (zerovalue._1 + number, zerovalue._2 + 1)
    }
    val combOp = (zerovaluePar1: (Int, Int), zerovaluePar2: (Int, Int)) => {
      (zerovaluePar1._1 + zerovaluePar2._1, zerovaluePar1._2 + zerovaluePar2._2)
    }

    rdd.par.aggregate(zerovalue)(seqOp, combOp)

    /*
      cogroup
     */

    sc.stop()
  }
}
