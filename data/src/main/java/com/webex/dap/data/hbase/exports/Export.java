package com.webex.dap.data.hbase.exports;

import com.webex.dap.data.hbase.mapreduce.HBaseCsvOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.IdentityTableMapper;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
/**
 * Created by harry on 1/30/18.
 *
 * export HBASE_CLASSPATH=/export/home/hewewu/data-1.1.jar:$HBASE_CLASSPATH
 * hbase com.webex.dap.data.hbase.exports.Export -D hbase.zookeeper.quorum=rphf95kaf001.qa.webex.com,rphf95kaf002.qa.webex.com,rphf95kaf003.qa.webex.com -D mapreduce.output.hbasecsvoutputformat.columns=f1:c1,f2:c2 csv_tt /tmp/csv_tt
 */
public class Export{
//    private static final Log LOG = LogFactory.getLog(Export.class);

    final static String NAME = "export";
    final static String RAW_SCAN = "hbase.mapreduce.include.deleted.rows";
    final static String EXPORT_BATCHING = "hbase.export.scanner.batch";
    final static String SINGLE_COLUMN_FILTER = "hbase.mapreduce.single.column.filter";

    public static Job createSubmittableJob(Configuration conf, String[] args)
            throws IOException {
        String tableName = args[0];
        Path outputDir = new Path(args[1]);
        Job job = new Job(conf, NAME + "_" + tableName);
        job.setJobName(NAME + "_" + tableName);
        job.setJarByClass(Export.class);

        // Set optional scan parameters
        Scan s = getConfiguredScanForJob(conf, args);
        IdentityTableMapper.initJob(tableName, s, IdentityTableMapper.class, job);
        // No reducers.  Just write straight to output files.
        job.setNumReduceTasks(0);
        job.setOutputFormatClass(HBaseCsvOutputFormat.class);
        job.setOutputKeyClass(ImmutableBytesWritable.class);
        job.setOutputValueClass(Result.class);
        FileOutputFormat.setOutputPath(job, outputDir); // job conf doesn't contain the conf so doesn't have a default fs.
        return job;
    }

    private static Scan getConfiguredScanForJob(Configuration conf, String[] args) throws IOException {
        Scan s = new Scan();

        // Optional arguments.
        // Set Scan Versions
        int versions = args.length > 2 ? Integer.parseInt(args[2]) : 1;
        s.setMaxVersions(versions);
        // Set Scan Range
        long startTime = args.length > 3 ? Long.parseLong(args[3]) : 0L;
        long endTime = args.length > 4 ? Long.parseLong(args[4]) : Long.MAX_VALUE;
        s.setTimeRange(startTime, endTime);
        // Set cache blocks
        s.setCacheBlocks(false);
        // set Start and Stop row
        if (conf.get(TableInputFormat.SCAN_ROW_START) != null) {
            s.setStartRow(Bytes.toBytes(conf.get(TableInputFormat.SCAN_ROW_START)));
        }
        if (conf.get(TableInputFormat.SCAN_ROW_STOP) != null) {
            s.setStopRow(Bytes.toBytes(conf.get(TableInputFormat.SCAN_ROW_STOP)));
        }
        // Set Scan Column Family
        boolean raw = Boolean.parseBoolean(conf.get(RAW_SCAN));
        if (raw) {
            s.setRaw(raw);
        }

        if (conf.get(TableInputFormat.SCAN_COLUMN_FAMILY) != null) {
            s.addFamily(Bytes.toBytes(conf.get(TableInputFormat.SCAN_COLUMN_FAMILY)));
        }
        // Set RowFilter or Prefix Filter if applicable.
        Filter exportFilter = getExportFilter(args);
        if (exportFilter != null) {
//            LOG.info("Setting Scan Filter for Export.");
            s.setFilter(exportFilter);
        }

        if (conf.get(SINGLE_COLUMN_FILTER) != null){
            FilterList filter = new FilterList();
            Filter orignalFilter = s.getFilter();

            if (orignalFilter != null){
                filter.addFilter(orignalFilter);
            }

            String singleColumnFilter = conf.get(SINGLE_COLUMN_FILTER);
            String[] parts = singleColumnFilter.split("=",2);
            String[] identifier = parts[0].split(":");
            String value = parts[1];

            SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(Bytes.toBytes(identifier[0]),Bytes.toBytes(identifier[1]), CompareFilter.CompareOp.EQUAL,Bytes.toBytes(value));
            filter.addFilter(singleColumnValueFilter);
            s.setFilter(filter);
        }

        int batching = conf.getInt(EXPORT_BATCHING, -1);
        if (batching != -1) {
            try {
                s.setBatch(batching);
            } catch (IncompatibleFilterException e) {
//                LOG.error("Batching could not be set", e);
            }
        }
//        LOG.info("versions=" + versions + ", starttime=" + startTime + ", endtime=" + endTime + ", keepDeletedCells=" + raw);
        return s;
    }

    private static Filter getExportFilter(String[] args) {
        Filter exportFilter = null;
        String filterCriteria = (args.length > 5) ? args[5]: null;
        if (filterCriteria == null) return null;
        if (filterCriteria.startsWith("^")) {
            String regexPattern = filterCriteria.substring(1, filterCriteria.length());
            exportFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator(regexPattern));
        } else {
            exportFilter = new PrefixFilter(Bytes.toBytes(filterCriteria));
        }
        return exportFilter;
    }

    /*
   * @param errorMsg Error message.  Can be null.
   */
    private static void usage(final String errorMsg) {
        if (errorMsg != null && errorMsg.length() > 0) {
            System.err.println("ERROR: " + errorMsg);
        }
        System.err.println("Usage: Export [-D <property=value>]* <tablename> <outputdir> [<versions> " +
                "[<starttime> [<endtime>]] [^[regex pattern] or [Prefix] to filter]]\n");
        System.err.println("  Note: -D properties will be applied to the conf used. ");
        System.err.println("   -D mapreduce.output.hbasecsvoutputformat.columns=  (must be set,comma-separate)");
        System.err.println("  For example: ");
        System.err.println("   -D mapreduce.output.hbasecsvoutputformat.separator=,");
        System.err.println("   -D mapreduce.output.hbasecsvoutputformat.header=true");

//        System.err.println("   -D mapreduce.output.fileoutputformat.compress=true");
//        System.err.println("   -D mapreduce.output.fileoutputformat.compress.codec=org.apache.hadoop.io.compress.GzipCodec");
//        System.err.println("   -D mapreduce.output.fileoutputformat.compress.type=BLOCK");
        System.err.println("  Additionally, the following SCAN properties can be specified");
        System.err.println("  to control/limit what is exported..");
        System.err.println("   -D " + TableInputFormat.SCAN_COLUMN_FAMILY + "=<familyName>");
        System.err.println("   -D " + RAW_SCAN + "=true");
        System.err.println("   -D " + TableInputFormat.SCAN_ROW_START + "=<ROWSTART>");
        System.err.println("   -D " + TableInputFormat.SCAN_ROW_STOP + "=<ROWSTOP>");
        System.err.println("   -D " + SINGLE_COLUMN_FILTER + "=<familyName>:<qualifier>=<value>");
        System.err.println("For performance consider the following properties:\n"
                + "   -Dhbase.client.scanner.caching=100\n"
                + "   -Dmapreduce.map.speculative=false\n"
                + "   -Dmapreduce.reduce.speculative=false");
        System.err.println("For tables with very wide rows consider setting the batch size as below:\n"
                + "   -D" + EXPORT_BATCHING + "=10");
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        String columns = conf.get("mapreduce.output.hbasecsvoutputformat.columns");
        if (otherArgs.length < 2 || columns == null || columns.trim().length() == 0) {
            usage("Wrong number of arguments: " + otherArgs.length);
            System.exit(-1);
        }
        Job job = createSubmittableJob(conf, otherArgs);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
