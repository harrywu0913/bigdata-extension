#!/usr/bin/env bash


/opt/cloudera/parcels/KAFKA/lib/kafka/bin/kafka-consumer-groups.sh --bootstrap-server rphf1kaf001.qa.webex.com:9092 --describe --group hf1_test

hadoop fs -ls /tmp/hdfs-test/hf1_logstash_meeting_hdfs/*/*/*/*.tmp

#hadoop fs -rm -r -skipTrash /tmp/hdfs-test/*
echo `date +"%F %H-%M-%S"` > start_time.log; cat 1G.txt | kafka-console-producer --broker-list rphf1kaf001.qa.webex.com:9092,rphf1kaf002.qa.webex.com:9092,rphf1kaf003.qa.webex.com:9092 --topic logstash_meeting_hdfs_test;echo `date +"%F %H-%M-%S"` > end_time.log;



time_end=`hadoop fs -stat /tmp/hdfs-test/hf1_logstash_meeting_hdfs/*/*/2018-07-16 | sort -r | head -n 1`
raw_time_end=`hadoop fs -stat /tmp/hdfs-test/hf1_logstash_meeting_hdfs_raw//2018-07-16 | sort -r | head -n 1`
total_num=`hadoop fs -text /tmp/hdfs-test/hf1_logstash_meeting_hdfs_raw/2018-07-16/* | wc -l`
raw_total_num=`hadoop fs -text /tmp/hdfs-test/hf1_logstash_meeting_hdfs/*/*/2018-07-16/* | wc -l`
size=`hadoop fs -du -s /tmp/hdfs-test/hf1_logstash_meeting_hdfs/*/*/2018-07-16 | awk '{print $1}' | awk '{SUM += $1} END {print SUM}'`
raw_size=`hadoop fs -du -s /tmp/hdfs-test/hf1_logstash_meeting_hdfs_raw/2018-07-16 | awk '{print $1}' | awk '{SUM += $1} END {print SUM}'`


#if [ -f $time_end.log ];
#then
#    time_start=`cat $time_end.log`
#else
#    time_start=`cat start_time.log`
#fi

#if [ -f $raw_time_end.log ];
#then
#    raw_time_start=`cat $raw_time_end.log`
#else
#    raw_time_start=`cat start_time.log`
#fi

#echo $time_end > time_end.log
#echo $raw_time_end > raw_time_end.log

start_time=`cat start_time.log`
let raw_time_cost=(`date -d "${raw_time_end}" +%s` - `date -d "${start_time}" +%s`)
let time_cost=(`date -d "${time_end}" +%s` - `date -d "${start_time}" +%s`)

echo "start_time: ${start_time}"
echo -e "\t\t\traw\t\t\t\tnew"
echo -e "total_num\t\t${raw_total_num}\t\t\t\t${total_num}"
echo -e "size\t\t\t${raw_size}\t\t\t\t${size}"
#echo -e "time_start\t\t${raw_time_start}\t\t${time_start}"
echo -e "time_end\t\t${raw_time_end}\t\t${time_end}"
echo -e "time_cost\t\t${raw_time_cost}\t\t\t${time_cost}"