package com.paem.config;

import org.springframework.core.NamedThreadLocal;

public class ProcessContext {

    private static final ThreadLocal<String> currentProcessInstanceId = new NamedThreadLocal<>("ProcessContext");

    public static final String getCurrentProcessInstanceId() {
        return currentProcessInstanceId.get();
    }

    public static final void setCurrentProcessInstanceId(String processInstanceId) {
        currentProcessInstanceId.set(processInstanceId);
    }

    public static void cleanup() {
        currentProcessInstanceId.remove();
    }
}
