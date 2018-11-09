package com.webex.dap.data.hbase.mapreduce;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by harry on 1/30/18.
 */
public class HBaseCsvOutputFormat extends FileOutputFormat<ImmutableBytesWritable, Result> {
    public static String SEPERATOR = "mapreduce.output.hbasecsvoutputformat.separator";
    public static String COLUMNS = "mapreduce.output.hbasecsvoutputformat.columns";
    public static String HEADER = "mapreduce.output.hbasecsvoutputformat.header";
    private static final String utf8 = "UTF-8";

    private static final byte[] newline;

    static {
        try {
            newline = "\n".getBytes(utf8);
        } catch (UnsupportedEncodingException uee) {
            throw new IllegalArgumentException("can't find " + utf8 + " encoding");
        }
    }

    public RecordWriter<ImmutableBytesWritable, Result> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
        Configuration conf = job.getConfiguration();
        String valueSeparator = conf.get(SEPERATOR, ",");
        String columns = conf.get(COLUMNS);

        if (columns == null || columns.trim().length() == 0)
            throw new IOException("mapreduce.output.hbasecsvoutputformat.columns should be set");


        String extension = ".csv";

//        boolean isCompressed = getCompressOutput(job);
//        CompressionCodec codec = null;
//        if (isCompressed) {
//            Class<? extends CompressionCodec> codecClass =
//                    getOutputCompressorClass(job, GzipCodec.class);
//            codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, conf);
//            extension = codec.getDefaultExtension();
//        }

        Path file = getDefaultWorkFile(job, extension);
        FileSystem fs = file.getFileSystem(conf);

        DataOutputStream fileOut = fs.create(file, false);

//        if (isCompressed){
//            fileOut = new DataOutputStream(codec.createOutputStream(fileOut));
//        }

        boolean header = conf.getBoolean(HEADER,false);
        if (header) {
            fileOut.write(new StringBuilder().append(",").append(columns.toLowerCase()).toString().getBytes("UTF-8"));
            fileOut.write(newline);
        }

        return new HBaseCsvRecordWriter(fileOut, valueSeparator, columns.toLowerCase());
    }

    protected static class HBaseCsvRecordWriter extends RecordWriter<ImmutableBytesWritable, Result> {
        protected DataOutputStream out;
        protected String valueSeparator;
        protected List<String> columns = new ArrayList<String>();

        public HBaseCsvRecordWriter(DataOutputStream out, String valueSeparator, String columns) {
            this.out = out;
            this.valueSeparator = valueSeparator;
            this.columns.addAll(Arrays.asList(columns.split(",")));
        }

        public void write(ImmutableBytesWritable key, Result value) throws IOException, InterruptedException {
            boolean nullValue = value == null;

            if (nullValue) {
                return;
            }

            if (!nullValue) {
                List<Cell> cells = value.listCells();

                Map<String, String> recordsMap = new HashMap<String, String>();

                String row = "";
                for (Cell cell : cells) {
                    row = new String(CellUtil.cloneRow(cell));
                    String cf = new String(CellUtil.cloneFamily(cell));
                    String qualifier = new String(CellUtil.cloneQualifier(cell));
                    String val = new String(CellUtil.cloneValue(cell));
                    recordsMap.put((cf + ":" + qualifier).toLowerCase(), val);
                }

                StringBuilder records = new StringBuilder();
                records.append(row).append(valueSeparator);
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
