package com.paem.work;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Memory {

    private Map<String, Map<String, ExecutionResult>> memory = new HashMap<>();

    public void put(String executionId, Map<String, ExecutionResult> params) {
        memory.put(executionId, params);
    }

    public Map<String, ExecutionResult> get(String executionId) {
        return memory.get(executionId);
    }

}
