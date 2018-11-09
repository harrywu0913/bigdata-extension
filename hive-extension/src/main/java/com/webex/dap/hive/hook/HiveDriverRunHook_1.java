package com.webex.dap.hive.hook;

import org.apache.hadoop.hive.ql.HiveDriverRunHook;
import org.apache.hadoop.hive.ql.HiveDriverRunHookContext;
import org.apache.hadoop.hive.ql.session.SessionState;

/**
 * Created by harry on 5/23/18.
 *
 * HiveDriverRunHook allows Hive to be extended with custom logic for processing commands
 */
public class HiveDriverRunHook_1 implements HiveDriverRunHook{
    /*
        Invoked before Hive begins any processing of a command in the Driver, notably before compilation and any customized performance logging.
     */
    @Override
    public void preDriverRun(HiveDriverRunHookContext hookContext) throws Exception {
        SessionState sessionState = SessionState.get();
        System.out.println(hookContext.getCommand());
    }

    /*
        Invoked after Hive performs any processing of a command, just before a response is returned to the entity calling the Driver
     */
    @Override
    public void postDriverRun(HiveDriverRunHookContext hookContext) throws Exception {
        SessionState sessionState = SessionState.get();
        System.out.println(hookContext.getCommand());
    }
}
