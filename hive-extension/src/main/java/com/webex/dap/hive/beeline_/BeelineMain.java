package com.webex.dap.hive.beeline_;

import org.apache.hive.beeline.BeeLine;

import java.io.IOException;

/**
 * Created by harry on 5/24/18.
 */
public class BeelineMain {
    public static void main(String[] args) throws IOException {
        BeeLine.main(new String[]{"-n","root","-u","jdbc:hive2://localhost:10000","-e","show current roles;"});
    }
}
