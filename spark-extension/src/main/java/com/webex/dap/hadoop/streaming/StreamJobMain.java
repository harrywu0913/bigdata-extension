package com.webex.dap.hadoop.streaming;

import org.apache.hadoop.streaming.HadoopStreaming;

/**
 * Created by harry on 6/8/18.
 */
public class StreamJobMain {
    public static void main(String[] args) throws Exception {
        HadoopStreaming.main(new String[]{"-input","/kafka/bt1_logstash_meeting_hdfs/2018-04-10","-output","/tmp/lzo_1"});
    }
}
