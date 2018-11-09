package com.webex.dap.data.java_.gc_;

/**
 * Created by harry on 9/17/18.
 * 新生代MinorGC
 */
public class GC_1 {
    private static final int _1MB = 1024 * 1024;

    /*
        -verbose:gc -Xmx20M -Xmx20M -Xmn10M -XX:PrintGCDetails -XX:SurvivorRatio=8
     */
    public static void main(String[] args) {
        byte[] allocation_1, allocation_2, allocation_3, allocation_4;

        allocation_1 = new byte[2 * _1MB];
        allocation_2 = new byte[2 * _1MB];
        allocation_3 = new byte[2 * _1MB];
        allocation_4 = new byte[4 * _1MB];

    }
}
