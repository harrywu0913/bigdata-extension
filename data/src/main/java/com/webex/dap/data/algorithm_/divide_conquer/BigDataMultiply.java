package com.webex.dap.data.algorithm_.divide_conquer;

public class BigDataMultiply {
    static long calculate(long x,long y,int n){
        if (x == 0 || y == 0)
            return 0;

        if (n == 1)
            return x * y;

        long a = (long) (x / Math.pow(10,n/2));
        long b = (long) (x % Math.pow(10,n/2));
        long c = (long) (y / Math.pow(10,n/2));
        long d = (long) (y % Math.pow(10,n/2));


        long ac = calculate(a,c,n/2);
        long bd = calculate(b,d,n/2);
        long ad = calculate(a,d,n/2);
        long bc = calculate(b,c,n/2);

        return (long) (Math.pow(10,n) * ac + (ad + bc) * Math.pow(10,n/2) + bd);
    }

    public static void main(String[] args){
        System.out.println(calculate(1234,5678,4));
        System.out.println(1234 * 5678);
    }
}
