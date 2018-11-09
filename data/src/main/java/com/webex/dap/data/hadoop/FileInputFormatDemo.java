package com.webex.dap.data.hadoop;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * Created by harry on 9/27/18.
 */
public class FileInputFormatDemo {
    public static void main(String[] args) throws IOException {
        FileInputFormat.setInputPaths(null,"/kafka/*_*_hdfs*/*/*/day=/*,/kafka/*_*_hdfs*/*/*");
    }
}
