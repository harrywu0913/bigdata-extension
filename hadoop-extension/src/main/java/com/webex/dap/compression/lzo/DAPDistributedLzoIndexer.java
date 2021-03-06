package com.webex.dap.compression.lzo;

import com.hadoop.compression.lzo.LzoIndex;
import com.hadoop.compression.lzo.LzopCodec;
import com.hadoop.mapreduce.LzoIndexOutputFormat;
import com.hadoop.mapreduce.LzoSplitInputFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DAPDistributedLzoIndexer extends Configured implements Tool {


    private static final Log LOG = LogFactory.getLog(DAPDistributedLzoIndexer.class);
    private final String LZO_EXTENSION = new LzopCodec().getDefaultExtension();

    private final PathFilter nonTemporaryFilter = new PathFilter() {
        @Override
        public boolean accept(Path path) {
            return !path.toString().endsWith("/_temporary");
        }
    };

    private void walkPath(Path path, PathFilter pathFilter, List<Path> accumulator) {
        try {
            FileSystem fs = path.getFileSystem(getConf());
            FileStatus fileStatus = fs.getFileStatus(path);

            if (fileStatus.isDirectory()) {
                FileStatus[] children = fs.listStatus(path, pathFilter);
                for (FileStatus childStatus : children) {
                    walkPath(childStatus.getPath(), pathFilter, accumulator);
                }
            } else if (path.toString().endsWith(LZO_EXTENSION)) {
                Path lzoIndexPath = path.suffix(LzoIndex.LZO_INDEX_SUFFIX);
                if (fs.exists(lzoIndexPath)) {
                    // If the index exists and is of nonzero size, we're already done.
                    // We re-index a file with a zero-length index, because every file has at least one block.
                    if (fs.getFileStatus(lzoIndexPath).getLen() > 0) {
                        LOG.info("[SKIP] LZO index file already exists for " + path);
                        return;
                    } else {
                        LOG.info("Adding LZO file " + path + " to indexing list (index file exists but is zero length)");
                        accumulator.add(path);
                    }
                } else {
                	if (fs.getFileStatus(path).getLen() > fs.getFileStatus(path).getBlockSize()) {
                		// If no index exists, we need to index the file.
                		LOG.info("LZO file " + path + ", Length ==>" + fs.getFileStatus(path).getLen() + "BlockSize ==> " + fs.getFileStatus(path).getBlockSize());
                		LOG.info("Adding LZO file " + path + " to indexing list (no index currently exists)");
                		accumulator.add(path);
					}
                }
            }
        } catch (IOException ioe) {
            LOG.warn("Error walking path: " + path, ioe);
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length == 0 || (args.length == 1 && "--help".equals(args[0]))) {
            printUsage();
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        List<Path> inputPaths = new ArrayList<Path>();
        for (String strPath : args) {
            walkPath(new Path(strPath), nonTemporaryFilter, inputPaths);
        }

        if (inputPaths.isEmpty()) {
            System.err.println("No input paths found - perhaps all "
                    + ".lzo files have already been indexed.");
            return 0;
        }

        Configuration conf = new Configuration();
        Job job = new Job(conf);
        job.setJobName("Distributed Lzo Indexer " + Arrays.toString(args));

        job.setOutputKeyClass(Path.class);
        job.setOutputValueClass(LongWritable.class);

    // The LzoIndexOutputFormat doesn't currently work with speculative execution.
        // Patches welcome.
        job.getConfiguration().setBoolean(
                "mapred.map.tasks.speculative.execution", false);

        job.setJarByClass(DAPDistributedLzoIndexer.class);
        job.setInputFormatClass(LzoSplitInputFormat.class);
        job.setOutputFormatClass(LzoIndexOutputFormat.class);
        job.setNumReduceTasks(0);
        job.setMapperClass(Mapper.class);

        for (Path p : inputPaths) {
            FileInputFormat.addInputPath(job, p);
        }

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new DAPDistributedLzoIndexer(), args);
        System.exit(exitCode);
    }

    public static void printUsage() {
        System.err.println("Usage: hadoop jar /path/to/this/jar com.hadoop.compression.lzo.DAPDistributedLzoIndexer <file.lzo | directory> [file2.lzo directory3 ...]");
    }
}
