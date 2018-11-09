package com.webex.dap.es.spark;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.elasticsearch.hadoop.cfg.ConfigurationOptions;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import scala.Tuple2;

import java.util.List;
import java.util.Map;

/**
 * Created by harry on 5/29/18.
 */
public class SparkES {

    public static void save(){
        SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("SparkESDemo");
        conf.set("es.nodes", "10.225.17.135");
        conf.set("es.port", "9200");
        conf.set("es.net.ssl", "true");
        conf.set("es.net.ssl.cert.allow.self.signed", "true");
        conf.set("es.net.http.auth.user", "clptest");
        conf.set("es.net.http.auth.pass", "clppass");

        JavaSparkContext jsc = new JavaSparkContext(conf);


        Map<String, ?> numbers = ImmutableMap.of("one", 1, "two", 2);
        Map<String, ?> airports = ImmutableMap.of("OTP", "Otopeni", "SFO", "San Fran");

        JavaRDD<Map<String, ?>> javaRDD = jsc.parallelize(ImmutableList.of(numbers, airports));
        JavaEsSpark.saveToEs(javaRDD, "test/test");

        JavaEsSpark.esRDD(jsc,"test/test");

        jsc.stop();
    }

    public static void read(){
        SparkConf conf = new SparkConf();
        conf.setMaster("local[*]");
        conf.setAppName("SparkESDemo");
        conf.set("es.nodes", "https://clpsj-bts-telephony.webex.com");
        conf.set("es.nodes.path.prefix","/elasticsearch");
        conf.set("es.port", "443");
        conf.set("es.net.ssl", "true");
//        conf.set("es.net.ssl.cert.allow.self.signed", "true");
        conf.set(ConfigurationOptions.ES_NODES_WAN_ONLY,"true");

        conf.set(ConfigurationOptions.ES_NET_HTTP_AUTH_USER,"es_admin_local");
        conf.set(ConfigurationOptions.ES_NET_HTTP_AUTH_PASS,"outyYPR8i0");

        JavaSparkContext jsc = new JavaSparkContext(conf);


        JavaPairRDD<String, Map<String, Object>> rdd = JavaEsSpark.esRDD(jsc,"metrics-clap_obt1-telephony-2018.10.28/cmssvr");

//        List<Tuple2<String, Map<String, Object>>> data = rdd.collect();

        System.out.println(rdd.count());

        jsc.stop();
    }

    public static void main(String[] args) {
        read();
    }
}
