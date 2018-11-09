package com.webex.dap.data.jdk_.signal_;

import sun.misc.Signal;
import sun.misc.SignalHandler;

public class SignalHandlerExample implements SignalHandler {
    @Override
    public void handle(Signal signal) {
        signalCallback(signal);
    }

    private void signalCallback(Signal sn) {
        System.out.println(sn.getName() + " is recevied.");
        System.exit(0);
    }

    public static void main(String[] args) throws InterruptedException {
        SignalHandlerExample handler = new SignalHandlerExample();

        Signal.handle(new Signal("TERM"), handler);
        Signal.handle(new Signal("INT"), handler);
        Signal.handle(new Signal("USR2"), handler);

        while (true) {
            Thread.sleep(3000);
            System.out.println("running...");
        }
    }
}
