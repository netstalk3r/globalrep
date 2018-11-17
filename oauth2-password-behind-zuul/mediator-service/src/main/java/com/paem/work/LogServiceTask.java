package com.paem.work;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component("logger")
public class LogServiceTask implements NameAwarness {

    public void doWork(Map<String, ExecutionResult> params) {
        ExecutionResult result = new ExecutionResult();
        result.setTaskHash(hashCode());
        result.setThreadName(Thread.currentThread().getName());
        params.put(getName()+":doWork", result);
        System.out.println("first work");
    }

    public String doAnotherWork(Map<String, ExecutionResult> params) {
        ExecutionResult result = new ExecutionResult();
        result.setTaskHash(hashCode());
        result.setThreadName(Thread.currentThread().getName());
        String key = getName() + ":doAnotherWork";
        params.put(key, result);
        System.out.println("second work");
        return key;
    }

    public void printParams(Map<String, ExecutionResult> params) {
        System.out.println("param hash " + params.hashCode());
        System.out.println(String.valueOf(params));
    }

    @Override
    public String getName() {
        return "Logger";
    }
}
