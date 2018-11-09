package com.webex.dap.consolidate;

import com.hadoop.compression.lzo.LzopCodec;
import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileAlreadyExistsException;
import org.apache.hadoop.mapred.InvalidJobConfException;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

/**
 * Created by harry on 6/19/18.
 */
public class TelemetryConsolidate {
    public static class ConsolidateMapper extends Mapper<LongWritable, Text, NullWritable, Text> {
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            context.write(NullWritable.get(), value);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("consolidate.source.topic.prefix", "mc_telemetry_hdfs");
        conf.set("consolidate.output.dir", "/kafka-bak/mc_telemetry_hdfs");
        conf.set("consolidate.partition.key","day");
        conf.set("consolidate.day", "2018-06-18");

        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 2) {
            System.err.println("Usage: TelemetryConsolidate <in> <out>");
            System.exit(2);
        }
        Job job = Job.getInstance(conf, "TelemetryConsolidate");
        job.setJarByClass(TelemetryConsolidate.class);
        job.setMapperClass(ConsolidateMapper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);

        for (int i = 0; i < otherArgs.length - 1; ++i) {
            FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length - 1]));
        FileOutputFormat.setOutputCompressorClass(job, LzopCodec.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    class ConsolidateMultipleTextOutputFormat extends MultipleTextOutputFormat implements Configurable {
        Configuration conf;

        @Override
        protected String generateLeafFileName(String name) {
            StringBuilder finalname = new StringBuilder();
            if (conf.get("consolidate.partition.key") != null){
                finalname.append(conf.get("consolidate.source.topic.prefix")).append(conf.get("consolidate.day")).append(".").append(name);
            }else{
                finalname.append(conf.get("consolidate.source.topic.prefix")).append(conf.get("consolidate.day")).append(".").append(name);
            }
            return finalname.toString();
        }

        @Override
        public void setConf(Configuration conf) {
            this.conf = conf;
        }

        @Override
        public Configuration getConf() {
            return conf;
        }

        @Override
        public void checkOutputSpecs(FileSystem ignored, JobConf job) throws FileAlreadyExistsException, InvalidJobConfException, IOException {
        }
    }
}
