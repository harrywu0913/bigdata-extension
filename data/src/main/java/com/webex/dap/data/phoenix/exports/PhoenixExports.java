package com.webex.dap.data.phoenix.exports;

import com.google.common.collect.Lists;
import com.webex.dap.data.phoenix.mapreduce.PhoenixCsvOutputFormat;
import com.webex.dap.data.phoenix.model.PhoenixDBWritable;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.Export;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.phoenix.mapreduce.PhoenixInputFormat;
import org.apache.phoenix.mapreduce.util.ConnectionUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;

/**
 * Created by harry on 4/23/18.
 *
 * export HADOOP_CLASSPATH=/opt/cloudera/parcels/APACHE_PHOENIX/lib/phoenix/phoenix-4.7.0-clabs-phoenix1.3.0-client.jar:/export/home/hewewu/data-1.1.jar
 * hadoop jar data-1.1.jar com.webex.dap.data.phoenix.exports.PhoenixExports -D hbase.zookeeper.quorum=rpsj1hmn101.webex.com,rpsj1hmn102.webex.com:2181 SAP_IP2GEO /tmp/SAP_IP2GEO
 */
public class PhoenixExports {

    static class PhoenixExportMapper extends Mapper<NullWritable, PhoenixDBWritable, NullWritable, PhoenixDBWritable> {
        @Override
        protected void map(NullWritable key, PhoenixDBWritable value, Context context) throws IOException, InterruptedException {
            context.write(NullWritable.get(), value);
        }
    }

    /*
       -Dhbase.zookeeper.quorum=rpbt1hmn001.webex.com,rpbt1hmn002.webex.com,rpbt1hmn003.webex.com:2181 -Dzookeeper.znode.parent=/hbase <table_name> <output_dir>
     */
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        String[] remainArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

//        if (remainArgs.length < 2) {
//            System.err.println("Wrong number of arguments: " + remainArgs.length);
//            System.exit(-1);
//        }

        conf.set("hbase.zookeeper.quorum", "rpbt1hmn001.webex.com,rpbt1hmn002.webex.com,rpbt1hmn003.webex.com:2181");
        conf.set("zookeeper.znode.parent", "/hbase");
        conf.set("phoenix.input.table.name", "SAP_IP2GEO");
//        conf.set("phoenix.input.table.name", remainArgs[0]);
        conf.set("phoenix.input.class", PhoenixDBWritable.class.getName());


        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<String> columnNames = Lists.newArrayList();
        try {
            connection = ConnectionUtil.getInputConnection(conf);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from " + conf.get("phoenix.input.table.name") + " where 1 = 0");
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columns = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= columns; i++) {
                columnNames.add(resultSetMetaData.getColumnName(i).toLowerCase());
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }

            if (statement != null) {
                statement.close();
                statement = null;
            }

            if (connection != null) {
                connection.close();
                connection = null;
            }
        }

        if (CollectionUtils.isEmpty(columnNames)) {
            throw new IllegalArgumentException("");
        }

        conf.set("phoenix.mapreduce.output.columns", StringUtils.join(columnNames, ","));

        Job job = Job.getInstance(conf);
        job.setJarByClass(Export.class);

        job.setInputFormatClass(PhoenixInputFormat.class);
        job.setMapperClass(PhoenixExportMapper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(PhoenixDBWritable.class);

        job.setNumReduceTasks(0);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(PhoenixCsvOutputFormat.class);

        FileSystem fs = FileSystem.get(conf);

//        Path out = new Path(remainArgs[1]);
        Path out = new Path("/tmp/xx");

        if (fs.exists(out))
            fs.delete(out, true);

        FileOutputFormat.setOutputPath(job, out);

        TableMapReduceUtil.addDependencyJars(job);

        job.waitForCompletion(true);
    }
}

