package com.webex.dap.kafka.topic;

import kafka.admin.TopicCommand;

/**
 * Created by harry on 7/26/18.
 */
public class TopicCommandMain {
    public static void main(String[] args){
        TopicCommand.main("--zookeeper localhost:2181 --replication-factor 1 --partitions 2 --create --topic r1_p2_2".split(" "));
    }
}
