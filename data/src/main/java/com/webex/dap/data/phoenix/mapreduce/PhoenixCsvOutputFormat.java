package com.webex.dap.data.phoenix.mapreduce;

import com.webex.dap.data.phoenix.model.PhoenixDBWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by harry on 4/23/18.
 */
public class PhoenixCsvOutputFormat extends FileOutputFormat<NullWritable, PhoenixDBWritable> {
    public static String SEPERATOR = "phoenix.mapreduce.output.separator";
    public static String COLUMNS = "phoenix.mapreduce.output.columns";
    public static String HEADER = "phoenix.mapreduce.output.header";
    private static final String utf8 = "UTF-8";

    private static final byte[] newline;

    static {
        try {
            newline = "\n".getBytes(utf8);
        } catch (UnsupportedEncodingException uee) {
            throw new IllegalArgumentException("can't find " + utf8 + " encoding");
        }
    }

    @Override
    public RecordWriter<NullWritable, PhoenixDBWritable> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
        Configuration conf = job.getConfiguration();
        String valueSeparator = conf.get(SEPERATOR, ",");
        String columns = conf.get(COLUMNS);

        if (columns == null || columns.trim().length() == 0)
            throw new IOException("phoenix.mapreduce.output.columns should be set");


        String extension = ".csv";

        Path file = getDefaultWorkFile(job, extension);
        FileSystem fs = file.getFileSystem(conf);

        DataOutputStream fileOut = fs.create(file, false);

        boolean header = conf.getBoolean(HEADER, false);
        if (header) {
            fileOut.write(new StringBuilder().append(",").append(columns.toLowerCase()).toString().getBytes("UTF-8"));
            fileOut.write(newline);
        }

        return new PhoenixCsvRecordWriter(fileOut, valueSeparator, columns.toLowerCase());
    }

    protected static class PhoenixCsvRecordWriter extends RecordWriter<NullWritable, PhoenixDBWritable> {
        protected DataOutputStream out;
        protected String valueSeparator;
        protected List<String> columns = new ArrayList<String>();

        public PhoenixCsvRecordWriter(DataOutputStream out, String valueSeparator, String columns) {
            this.out = out;
            this.valueSeparator = valueSeparator;
            this.columns.addAll(Arrays.asList(columns.split(",")));
        }

        public void write(NullWritable key, PhoenixDBWritable value) throws IOException, InterruptedException {
            boolean nullValue = value == null;

            if (nullValue) {
                return;
            }

            if (!nullValue) {
                Map<String, Object> recordsMap = value.getResult();
                StringBuilder records = new StringBuilder();
                for (String col : this.columns) {
                    if (recordsMap.containsKey(col)) {
                        records.append(recordsMap.get(col)).append(valueSeparator);
                    } else {
                        records.append("").append(valueSeparator);
                    }
                }

                if (records.length() > 0) {
                    out.write(records.substring(0, records.length() - 1).toString().getBytes(utf8));
                    out.write(newline);
                }
            }
        }

        public void close(TaskAttemptContext context) throws IOException, InterruptedException {
            out.flush();
            out.close();
        }
    }
}
