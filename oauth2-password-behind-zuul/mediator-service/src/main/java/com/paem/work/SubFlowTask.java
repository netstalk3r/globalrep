package com.paem.work;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;

@Component("subFlow")
public class SubFlowTask implements NameAwarness {
    @Override
    public String getName() {
        return "subFlow";
    }

    public boolean random() {
        System.out.println(getName() + "%" + hashCode());
        return new Random().nextBoolean();
    }

    public void doTrue(Map<String, ExecutionResult> params) {
        ExecutionResult result = new ExecutionResult();
        result.setTaskHash(hashCode());
        result.setThreadName(Thread.currentThread().getName());
        String key = getName() + ":doTrue";
        params.put(key, result);
        System.out.println("true work");
    }

    public void doFalse(Map<String, ExecutionResult> params) {
        ExecutionResult result = new ExecutionResult();
        result.setTaskHash(hashCode());
        result.setThreadName(Thread.currentThread().getName());
        String key = getName() + ":doFalse";
        params.put(key, result);
        System.out.println("false work");
    }
}
