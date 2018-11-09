package org.apache.hadoop.fs.shell;

import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

public class DapPutComamnd {
    public static void registerCommands(CommandFactory factory) {
        factory.addClass(DapPut.class, "-dapput");
    }

    public static class DapPut extends DapCommandWithDestination {
        public static final String NAME = "dapput";
        public static final String USAGE = "[-f] [-p] [-l] <localsrc> ... <dst>";
        public static final String DESCRIPTION =
                "Copy files from the local file system " +
                        "into fs. Copying fails if the file already " +
                        "exists, unless the -f flag is given.\n" +
                        "Flags:\n" +
                        "  -p : Preserves access and modification times, ownership and the mode.\n" +
                        "  -f : Overwrites the destination if it already exists.\n" +
                        "  -l : Allow DataNode to lazily persist the file to disk. Forces\n" +
                        "       replication factor of 1. This flag will result in reduced\n" +
                        "       durability. Use with care.\n";

        @Override
        protected void processOptions(LinkedList<String> args) throws IOException {
            CommandFormat cf = new CommandFormat(1, Integer.MAX_VALUE, "f", "p", "l");
            cf.parse(args);
            setOverwrite(cf.getOpt("f"));
            setPreserve(cf.getOpt("p"));
            setLazyPersist(cf.getOpt("l"));
            getRemoteDestination(args);
            // should have a -r option
            setRecursive(true);
        }

        // commands operating on local paths have no need for glob expansion
        @Override
        protected List<PathData> expandArgument(String arg) throws IOException {
            List<PathData> items = new LinkedList<PathData>();
            try {
                items.add(new PathData(new URI(arg), getConf()));
            } catch (URISyntaxException e) {
                if (Path.WINDOWS) {
                    // Unlike URI, PathData knows how to parse Windows drive-letter paths.
                    items.add(new PathData(arg, getConf()));
                } else {
                    throw new IOException("unexpected URISyntaxException", e);
                }
            }
            return items;
        }

        @Override
        protected void processArguments(LinkedList<PathData> args)
                throws IOException {
            // NOTE: this logic should be better, mimics previous implementation
            if (args.size() == 1 && args.get(0).toString().equals("-")) {
                copyStreamToTarget(System.in, getTargetPath(args.get(0)));
                return;
            }
            super.processArguments(args);
        }
    }
}
