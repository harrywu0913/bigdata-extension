package com.webex.dap.data.java_.io_;

/*
    java.io     => 阻塞型
    java.nio    => 非阻塞型


    java.io
        分为三个部分：
            1. 流式部分                     ---    IO的主体部分
            2. 非流式部分                    ---   主要包含一些辅助类，如File/RandomAccessFile/FileDescriptor...
            3. 文件读取部分和安全相关的类        ---

        流式部分：
            两个对应和一个桥梁
                两个对应指：
                    1.  字节流和字符流
                    2.  输入和输出
                一个桥梁指：
                    从字节流到字符流的桥梁，对应于输入和输出就是InputStreamReader/OutputStreamWriter


    IO中输入字节流
        InputStream
            ByteArrayInputStream
            FileInputStream
            FilterInputStream
                BufferedInputStream
                DataInputStream
                LineNumberInputStream
                PushbackInputStream
            ObjectInputStream
            PipedInputStream
            SequenceInputStream
            StringBufferInputStream
    IO中输出字节流
    IO中输入字符流
    IO中输出字符流

 */
public class README {
}
