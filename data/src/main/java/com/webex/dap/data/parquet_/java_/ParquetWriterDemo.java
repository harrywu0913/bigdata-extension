package com.webex.dap.data.parquet_.java_;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.GroupFactory;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.hadoop.example.GroupWriteSupport;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;

import java.io.IOException;

/**
 * Created by harry on 7/6/18.
 */
public class ParquetWriterDemo {
    static MessageType schema = MessageTypeParser.parseMessageType("message Pair{\n" +
            " required binary city;\n" +
            " required binary ip;\n" +
            " repeated group time{\n" +
            "   required int32 tt1;\n" +
            "   required binary tt2;\n" +
            " }\n" +
            "}");

    static void parquetWriter(String outPath) throws IOException {
        GroupFactory factory = new SimpleGroupFactory(schema);
        Path path = new Path(outPath);

        Configuration configuration = new Configuration();
        GroupWriteSupport writeSupport = new GroupWriteSupport();
        writeSupport.setSchema(schema, configuration);

        ParquetWriter<Group> writer = new ParquetWriter<Group>(path, configuration, writeSupport);

        Group group = factory.newGroup().append("city", "sz").append("ip", "127.0.0.1");

        Group time1 = group.addGroup("time");
        time1.append("tt1",10).append("tt2","10_tt2");
        Group time2 = group.addGroup("time");
        time2.append("tt1",20).append("tt2","20_tt2");


        writer.write(group);

        writer.close();
    }

    static void parquetReader(String inPath) throws IOException{
        ParquetReader<Group> reader = new ParquetReader<Group>(new Path(inPath),new GroupReadSupport());

        Group line;
        while((line = reader.read()) != null){
            System.out.println(line);
        }

        reader.close();
    }

    public static void main(String[] args) throws IOException {
//        parquetWriter("/Users/harry/Documents/project/cmse/cloudtools-cdf/dap-extension/data/src/main/resources/parquet/parquet_test_1.parquet");
        parquetReader("/Users/harry/Documents/project/cmse/cloudtools-cdf/dap-extension/data/src/main/resources/parquet/parquet_test_1.parquet");
    }

}

