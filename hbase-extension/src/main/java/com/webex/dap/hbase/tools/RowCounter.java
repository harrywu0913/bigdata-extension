package com.webex.dap.hbase.tools;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by harry on 3/20/18.
 *
 * export HBASE_CLASSPATH=/export/home/hewewu/hello-scala-1.0-SNAPSHOT.jar:$HBASE_CLASSPATH
 * hbase com.webex.dap.hbase.tools.RowCounter -D hbase.zookeeper.quorum=rpsj1hmn101.webex.com:2181 -D hbase.mapreduce.single.column.filter="INFO:ENDTIME:\<:2018-08-02,INFO:ENDTIME:\>=:2018-08-01" XXRPT_HGSMEETINGREPORT_2018-08-02
 *
 */
public class RowCounter extends org.apache.hadoop.hbase.mapreduce.RowCounter {

    final static String SINGLE_COLUMN_FILTER = "hbase.mapreduce.single.column.filter";
    static final String NAME = "rowcounter";

    /**
     * Mapper that runs the count.
     */
    static class RowCounterMapper
            extends TableMapper<ImmutableBytesWritable, Result> {

        /**
         * Counter enumeration to count the actual rows.
         */
        public static enum Counters {
            ROWS
        }

        /**
         * Maps the data.
         *
         * @param row     The current table row key.
         * @param values  The columns.
         * @param context The current context.
         * @throws IOException When something is broken with the data.
         */
        @Override
        public void map(ImmutableBytesWritable row, Result values,
                        Context context)
                throws IOException {
            // Count every row containing data, whether it's in qualifiers or values
            context.getCounter(RowCounterMapper.Counters.ROWS).increment(1);
        }
    }

    /**
     * Sets up the actual job.
     *
     * @param conf The current configuration.
     * @param args The command line parameters.
     * @return The newly created job.
     * @throws IOException When setting up the job fails.
     */
    public static Job createSubmittableJob(Configuration conf, String[] args)
            throws IOException {
        String tableName = args[0];
        String startKey = null;
        String endKey = null;
        long startTime = 0;
        long endTime = 0;

        StringBuilder sb = new StringBuilder();

        final String rangeSwitch = "--range=";
        final String startTimeArgKey = "--starttime=";
        final String endTimeArgKey = "--endtime=";

        // First argument is table name, starting from second
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith(rangeSwitch)) {
                String[] startEnd = args[i].substring(rangeSwitch.length()).split(",", 2);
                if (startEnd.length != 2 || startEnd[1].contains(",")) {
                    printUsage("Please specify range in such format as \"--range=a,b\" " +
                            "or, with only one boundary, \"--range=,b\" or \"--range=a,\"");
                    return null;
                }
                startKey = startEnd[0];
                endKey = startEnd[1];
            }
            if (startTime < endTime) {
                printUsage("--endtime=" + endTime + " needs to be greater than --starttime=" + startTime);
                return null;
            }
            if (args[i].startsWith(startTimeArgKey)) {
                startTime = Long.parseLong(args[i].substring(startTimeArgKey.length()));
                continue;
            }
            if (args[i].startsWith(endTimeArgKey)) {
                endTime = Long.parseLong(args[i].substring(endTimeArgKey.length()));
                continue;
            } else {
                // if no switch, assume column names
                sb.append(args[i]);
                sb.append(" ");
            }
        }

        Job job = new Job(conf, NAME + "_" + tableName);
        job.setJarByClass(RowCounter.class);
        Scan scan = new Scan();
        scan.setCacheBlocks(false);
        Set<byte[]> qualifiers = new TreeSet<byte[]>(Bytes.BYTES_COMPARATOR);
        if (startKey != null && !startKey.equals("")) {
            scan.setStartRow(Bytes.toBytes(startKey));
        }
        if (endKey != null && !endKey.equals("")) {
            scan.setStopRow(Bytes.toBytes(endKey));
        }
        if (sb.length() > 0) {
            for (String columnName : sb.toString().trim().split(" ")) {
                String family = StringUtils.substringBefore(columnName, ":");
                String qualifier = StringUtils.substringAfter(columnName, ":");

                if (StringUtils.isBlank(qualifier)) {
                    scan.addFamily(Bytes.toBytes(family));
                } else {
                    scan.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
                }
            }
        }


        String singleColumnFilter = conf.get(SINGLE_COLUMN_FILTER);
        System.out.println("===========");
        System.out.println(singleColumnFilter);
        System.out.println("===========");

        String[] singleColumnFilters = singleColumnFilter.split(",");

        FilterList filter = new FilterList();

        for (String singleColumnValueFilter : singleColumnFilters){
            String[] parts = singleColumnValueFilter.split(":", 4);
            String cf = parts[0];
            String qualifier = parts[1];
            String operator = parts[2];
            String value = parts[3];

            CompareFilter.CompareOp op = null;
            switch (operator){
                case "<":
                    op = CompareFilter.CompareOp.LESS;
                    break;
                case "<=":
                    op = CompareFilter.CompareOp.LESS_OR_EQUAL;
                    break;
                case "=":
                    op = CompareFilter.CompareOp.EQUAL;
                    break;
                case "!=":
                    op = CompareFilter.CompareOp.NOT_EQUAL;
                    break;
                case ">=":
                    op = CompareFilter.CompareOp.GREATER_OR_EQUAL;
                    break;
                case ">":
                    op = CompareFilter.CompareOp.GREATER;
                    break;
                case "":
                    op = CompareFilter.CompareOp.NO_OP;
                    break;
                default:
                    op = CompareFilter.CompareOp.EQUAL;
                    break;
            }

            System.out.println("===========");
            System.out.println(cf + " " + qualifier + " " + op + " " + value);
            System.out.println("===========");

            filter.addFilter(new SingleColumnValueFilter(Bytes.toBytes(cf), Bytes.toBytes(qualifier), op, Bytes.toBytes(value)));
        }

        scan.setFilter(filter);


//        // specified column may or may not be part of first key value for the row.
//        // Hence do not use FirstKeyOnlyFilter if scan has columns, instead use
//        // FirstKeyValueMatchingQualifiersFilter.
//        if (qualifiers.size() == 0) {
//            scan.setFilter(new FirstKeyOnlyFilter());
//        } else {
//            scan.setFilter(new FirstKeyValueMatchingQualifiersFilter(qualifiers));
//        }

        scan.setTimeRange(startTime, endTime == 0 ? HConstants.LATEST_TIMESTAMP : endTime);
        job.setOutputFormatClass(NullOutputFormat.class);
        TableMapReduceUtil.initTableMapperJob(tableName, scan,
                RowCounterMapper.class, ImmutableBytesWritable.class, Result.class, job);
        job.setNumReduceTasks(0);
        return job;
    }

    private static void printUsage(String errorMessage) {
        System.err.println("ERROR: " + errorMessage);
        printUsage();
    }

    private static void printUsage() {
        System.err.println("Usage: RowCounter [options] <tablename> " +
                "[--starttime=[start] --endtime=[end] " +
                "[--range=[startKey],[endKey]] [<column1> <column2>...]");
        System.err.println("For performance consider the following options:\n"
                + "-Dhbase.client.scanner.caching=100\n"
                + "-Dmapreduce.map.speculative=false");
    }

    /**
     * Main entry point.
     *
     * @param args The command line parameters.
     * @throws Exception When running the job fails.
     */
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length < 1) {
            printUsage("Wrong number of parameters: " + args.length);
            System.exit(-1);
        }
        Job job = createSubmittableJob(conf, otherArgs);
        if (job == null) {
            System.exit(-1);
        }
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
