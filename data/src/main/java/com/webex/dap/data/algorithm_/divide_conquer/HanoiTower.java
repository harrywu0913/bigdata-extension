package com.webex.dap.data.algorithm_.divide_conquer;

import java.util.List;

public class HanoiTower {
    public static void move(int n, List<Integer> a, List<Integer> b, List<Integer> c) {
        if (n == 0)
            return;
        move(n - 1, a, c, b);
        c.add(a.get(0));
        move(n - 1, b, a, c);

    }
}
