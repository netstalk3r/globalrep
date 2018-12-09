package com.paem.work;

import java.io.Serializable;

public class ExecutionResult implements Serializable {

    private String result;
    private int taskHash;
    private String threadName;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getTaskHash() {
        return taskHash;
    }

    public void setTaskHash(int taskHash) {
        this.taskHash = taskHash;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public String toString() {
        return "ExecutionResult{" +
                "result='" + result + '\'' +
                ", taskHash=" + taskHash +
                ", threadName='" + threadName + '\'' +
                '}';
    }
}
