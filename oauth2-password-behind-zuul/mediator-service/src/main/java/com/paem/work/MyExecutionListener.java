package com.paem.work;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("executionListener")
public class MyExecutionListener {

    @Autowired
    private Memory memory;

    public void notify(DelegateExecution execution) {
        System.out.println("in listener");
        Map<String, ExecutionResult> params = (Map<String, ExecutionResult>) execution.getVariable("params");
        memory.put(execution.getId(), params);
    }
}
