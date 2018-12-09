package com.paem.work;

import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("endExecutionListener")
public class EndEventExecutionListener {

    @Autowired
    private Memory memory;

    public void notify(DelegateExecution execution) {
        memory.put(execution.getProcessInstanceId(), (Map<String, ExecutionResult>) execution.getVariable("params"));
    }
}
