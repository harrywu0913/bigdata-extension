package com.webex.dap.data.java_.io_;

import java.io.*;

public class FileDescriptorDemo {
    public static void sync() throws Exception {
        OutputStream out2 = new FileOutputStream(new File("/Users/harry/Documents/project/cmse/cloudtools-cdf/dap-extension/data/src/main/resources/utf8_out_3.txt"));
        FileDescriptor fd2 = ((FileOutputStream) out2).getFD();

        out2.write("你好".getBytes());

        //刷新缓冲区，但是数据可能没有写入到磁盘
        /*
            Flushes this output stream and forces any buffered output bytes to be written out.
            If the intended destination of this stream is an abstraction provided by the underlying operating system.
            for example a file, then flushing the stream guarantees only that bytes previously written to the stream are passed to the operating system for writing;
            it does not guarantee that they are actually written to a physical device such as a disk drive.
         */
        out2.flush();


        // 阻塞直到数据缓冲区的数据全部写入到磁盘，该方法返回后，数据已经写入到磁盘了。
        fd2.sync();

        out2.close();
    }

    public static void same_fd() throws Exception {
        OutputStream out1 = new FileOutputStream(new File("/Users/harry/Documents/project/cmse/cloudtools-cdf/dap-extension/data/src/main/resources/utf8_out_4.txt"));
        FileDescriptor fd = ((FileOutputStream) out1).getFD();

        OutputStream out2 = new FileOutputStream(fd);

        out1.write("你好".getBytes());
        out2.write("吗？".getBytes());


        if (fd != null) {
            System.out.println(fd.valid());
        }

        out2.close();
        out1.close();
    }

    public static void different_fd() throws Exception {
        OutputStream out1 = new FileOutputStream(new File("/Users/harry/Documents/project/cmse/cloudtools-cdf/dap-extension/data/src/main/resources/utf8_out_5.txt"));
        OutputStream out2 = new FileOutputStream(new File("/Users/harry/Documents/project/cmse/cloudtools-cdf/dap-extension/data/src/main/resources/utf8_out_5.txt"));

        out1.write("你好".getBytes());
        out2.write("吗？".getBytes());


        System.out.println(((FileOutputStream) out1).getFD() == ((FileOutputStream) out2).getFD());

        out2.close();
        out1.close();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("fd");

        PrintStream out = new PrintStream(new FileOutputStream(FileDescriptor.out));
        out.println("fd");
        out.close();

        InputStream input = new FileInputStream(new File("/Users/harry/Documents/project/cmse/cloudtools-cdf/dap-extension/data/src/main/resources/gbk.txt"));
        System.out.println(((FileInputStream) input).getFD());
        input.close();


    }
}
