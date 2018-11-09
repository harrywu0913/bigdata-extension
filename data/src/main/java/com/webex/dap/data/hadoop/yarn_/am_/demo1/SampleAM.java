package com.webex.dap.data.hadoop.yarn_.am_.demo1;

import com.beust.jcommander.JCommander;

import java.util.List;

/**
 * Created by harry on 9/27/18.
 */
public class SampleAM extends AbstractApplicationMaster {
    public SampleAM() {
        super();
    }

    @Override
    protected List<String> buildCommandList(int startFrom, int containerCnt, String command) {
        return null;
    }

    public static void main(String[] args) {
        System.out.println("ApplicationMaster::main"); //xxx
        AbstractApplicationMaster am = new SampleAM();
        new JCommander(am, args);
        am.init(args);

        try {
            am.run();
        } catch (Exception e) {
            System.out.println("am.run throws: " + e);
            e.printStackTrace();
            System.exit(0);
        }
    }
}

