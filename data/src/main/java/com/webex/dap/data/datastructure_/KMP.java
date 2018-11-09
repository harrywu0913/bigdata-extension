package com.webex.dap.data.datastructure_;

public class KMP {
    public static int bf(String ts, String ps) {
        char[] t = ts.toCharArray();
        char[] p = ps.toCharArray();

        int i = 0;
        int j = 0;

        while (i < t.length && j < p.length) {
            if (t[i] == p[j]) {
                i++;
                j++;
            } else {
                // 一旦不匹配，i后退
                i = i - j + 1;
                // j归0
                j = 0;
            }
        }

        if (j == p.length) {
            return i - j;
        } else {
            return -1;
        }
    }

    public static int kmp(String ts,String ps){
        char[] t = ts.toCharArray();
        char[] p = ps.toCharArray();

        int i = 0;
        int j = 0;

        int[] next = getNext(ps);

        while (i < t.length && j < p.length) {
            if (t[i] == p[j]) {
                i++;
                j++;
            } else {
//                i = i - j + 1;  i 不需要回溯
//                j = 0;
                j = next[j];    //  j回到指定位置
            }
        }

        if (j == p.length) {
            return i - j;
        } else {
            return -1;
        }
    }

    private static int[] getNext(String ps) {
        return new int[0];
    }
}
