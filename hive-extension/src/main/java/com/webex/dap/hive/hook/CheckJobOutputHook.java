package com.webex.dap.hive.hook;

import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.exec.FileSinkOperator;
import org.apache.hadoop.hive.ql.exec.Operator;
import org.apache.hadoop.hive.ql.exec.Task;
import org.apache.hadoop.hive.ql.exec.TaskRunner;
import org.apache.hadoop.hive.ql.exec.mr.MapRedTask;
import org.apache.hadoop.hive.ql.hooks.ExecuteWithHookContext;
import org.apache.hadoop.hive.ql.hooks.HookContext;
import org.apache.hadoop.hive.ql.plan.FileSinkDesc;
import org.apache.hadoop.hive.ql.plan.OperatorDesc;
import org.apache.hadoop.hive.ql.plan.ReduceWork;

import java.io.Serializable;
import java.util.List;

/**
 * Created by harry on 6/4/18.
 */
public class CheckJobOutputHook implements ExecuteWithHookContext {
    @Override
    public void run(HookContext hookContext) throws Exception {
        List<TaskRunner> trs = hookContext.getCompleteTaskList();
        HiveConf conf = hookContext.getConf();

        if (CollectionUtils.isNotEmpty(trs)) {
            for (TaskRunner tr : trs) {
                Task<? extends Serializable> task = tr.getTask();
                if (task instanceof MapRedTask) {
                    MapRedTask mrTask = (MapRedTask) task;
                    ReduceWork reduceWork = mrTask.getWork().getReduceWork();

                    int reducerNumber = reduceWork.getNumReduceTasks();

                    Operator<? extends OperatorDesc> reducer = mrTask.getWork().getReduceWork().getReducer();

                    if (reducerNumber != 0 && reducer != null) {
                        FileSinkOperator fsop = getFileSinkOperator(reducer);
                        if (fsop != null){
                            FileSinkDesc fsDesc = fsop.getConf();
                            fsDesc.getDirName();
                        }
                    }
                }
            }
        }
    }

    private FileSinkOperator getFileSinkOperator(Operator<? extends OperatorDesc> operator) {
        if (operator instanceof FileSinkOperator) {
            if (operator.getChildOperators() != null && operator.getChildOperators().size() > 0) {
            }
            return (FileSinkOperator) operator;
        } else {
            List<Operator<? extends OperatorDesc>> childList = operator.getChildOperators();
            if (childList != null) {
                for (Operator op : childList) {
                    FileSinkOperator re = getFileSinkOperator(op);
                    if (re != null) return re;
                }
            }
            return null;
        }
    }
}
