package com.paem.work;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Memory {

    private Map<String, Map<String, ExecutionResult>> memory = new HashMap<>();

    public void put(String processInstanceId, Map<String, ExecutionResult> params) {
        memory.put(processInstanceId, params);
    }

    public Map<String, ExecutionResult> get(String processInstanceId) {
        return memory.get(processInstanceId);
    }

}
