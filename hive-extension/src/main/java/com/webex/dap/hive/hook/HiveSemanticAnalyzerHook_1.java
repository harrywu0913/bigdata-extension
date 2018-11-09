package com.webex.dap.hive.hook;

import org.apache.hadoop.hive.ql.exec.Task;
import org.apache.hadoop.hive.ql.hooks.ReadEntity;
import org.apache.hadoop.hive.ql.hooks.WriteEntity;
import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.apache.hadoop.hive.ql.parse.HiveSemanticAnalyzerHook;
import org.apache.hadoop.hive.ql.parse.HiveSemanticAnalyzerHookContext;
import org.apache.hadoop.hive.ql.parse.SemanticException;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by harry on 7/18/18.
 */
public class HiveSemanticAnalyzerHook_1 implements HiveSemanticAnalyzerHook {
    @Override
    public ASTNode preAnalyze(HiveSemanticAnalyzerHookContext context, ASTNode ast) throws SemanticException {
        System.out.println(context);
        System.out.println(ast);
        return ast;
    }

    @Override
    public void postAnalyze(HiveSemanticAnalyzerHookContext context, List<Task<? extends Serializable>> rootTasks) throws SemanticException {
        System.out.println(context);
        System.out.println(rootTasks);

        Set<ReadEntity> inputs = context.getInputs();

        if (inputs != null && inputs.size() > 0){
            for(ReadEntity entity : inputs){
                System.out.println(entity.getDatabase());
                System.out.println(entity.getTable());
                System.out.println(entity.getPartition());

                System.out.println(entity.getParents());

                System.out.println(entity.getAccessedColumns());
            }
        }

        Set<WriteEntity> outputs = context.getOutputs();

        if (outputs != null && outputs.size() > 0){
            for(WriteEntity entity : outputs){
                System.out.println(entity.getD());
            }
        }
    }
}
