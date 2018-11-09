package com.webex.dap.es.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.elasticsearch.hadoop.cfg.ConfigurationOptions;
import org.elasticsearch.hadoop.mr.EsInputFormat;

import java.io.IOException;

/**
 * Created by harry on 6/6/18.
 */
public class HadoopES {

    public static class ESMapper extends Mapper {
        @Override
        protected void map(Object key, Object value, Context context) throws IOException, InterruptedException {
            context.write((Text) key, (Text) value);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("mapreduce.framework.name","local");

        conf.set(ConfigurationOptions.ES_NODES, "https://clpsj-bts-telephony.webex.com");
        conf.set(ConfigurationOptions.ES_NODES_PATH_PREFIX, "/elasticsearch");
//        conf.set(ConfigurationOptions.ES_NET_USE_SSL,"true");
        conf.set(ConfigurationOptions.ES_PORT, "443");

        conf.set(ConfigurationOptions.ES_NODES_WAN_ONLY, "true");
//        conf.set(ConfigurationOptions.ES_NODES_CLIENT_ONLY, "true");

//        conf.set(ConfigurationOptions.ES_INPUT_JSON, "yes");
        conf.set(ConfigurationOptions.ES_OUTPUT_JSON,"yes");

//        es_admin_local:outyYPR8i0
        conf.set(ConfigurationOptions.ES_NET_HTTP_AUTH_USER, "hewewu");
        conf.set("es.resource", "/metrics-clap_obt1-telephony-2018.10.28/cmssvr");
        conf.set("es.query", "?q=me*");

        Job job = Job.getInstance(conf);
        job.setInputFormatClass(EsInputFormat.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(MapWritable.class);
        job.setMapperClass(ESMapper.class);
        job.setNumReduceTasks(0);

        FileOutputFormat.setOutputPath(job, new Path("/tmp/es_output_5/"));

        job.waitForCompletion(true);
    }
}

