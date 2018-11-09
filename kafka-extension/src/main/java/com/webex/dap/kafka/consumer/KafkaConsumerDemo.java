package com.webex.dap.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * Created by harry on 1/23/18.
 * <p>
 * kafka-topics --zookeeper rphf1kaf001.qa.webex.com:2181 --create --replication-factor 2 --partitions 3 --topic foo
 * <p>
 * kafka-console-producer --broker-list rphf1kaf001.qa.webex.com:9092,rphf1kaf002.qa.webex.com:9092,rphf1kaf003.qa.webex.com:9092 --topic foo
 */
public class KafkaConsumerDemo {
    //    static Gson gson = new Gson();
    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();
//        props.put("bootstrap.servers", "localhost:9092");
//        props.put("bootstrap.servers", "rphf1kaf001.qa.webex.com:9092,rphf1kaf002.qa.webex.com:9092,rphf1kaf003.qa.webex.com:9092");
//        props.put("bootstrap.servers", "rpbt1hsn008.webex.com:9092,rpbt1hsn009.webex.com:9092,rpbt1hsn010.webex.com:9092");
//        props.put("bootstrap.servers", "bt1-kafka-s.webex.com:9092");
        props.put("bootstrap.servers", "rphf1kaf011.qa.webex.com:9092,rphf1kaf012.qa.webex.com:9092");

        props.put("group.id", "consumer-tutorial-xxxx");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
//        props.put("enable.auto.commit", "false");
//        props.put("auto.offset.reset","earliest");
//        props.put("max.poll.records","10");

//        props.put("max.poll.interval.ms",5 * 1000);

        final KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
//        consumer.subscribe(Arrays.asList("foo", "bar"));
        consumer.subscribe(Arrays.asList("100M_test"), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                System.out.println("onPartitionsRevoked");
                for (TopicPartition tp : partitions) {
                    System.out.println("topic:{" + tp.topic() + "} partitions:{" + tp.partition() + "}");
                }

//                consumer.commitSync();
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                System.out.println("onPartitionsAssigned");
                for (TopicPartition tp : partitions) {
                    System.out.println("topic:{" + tp.topic() + "} partitions:{" + tp.partition() + "}");
                }
            }
        });

        int msgNo = 0;

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            Thread.sleep(1 * 1000);
            int i = 0;
            System.out.println(records.count());
//            for (ConsumerRecord<String, String> record : records) {
//                Thread.sleep(1 * 1000);
//                System.out.println(record);
//                Map<String, Object> data = new HashMap<String, Object>();
//                data.put("no", msgNo);
//                data.put("partition", record.partition());
//                data.put("offset", record.offset());
//                data.put("value", record.value());
//                data.put("i", i);
//                System.out.println(data);
//                i++;
//
//                if (i == 2){
////                    consumer.commitSync();
//                    System.out.println("commitSync");
//                }
//            }
//            msgNo++;
        }
    }
}
