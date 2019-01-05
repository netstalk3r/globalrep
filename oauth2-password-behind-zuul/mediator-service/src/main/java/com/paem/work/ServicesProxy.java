package com.paem.work;

import com.paem.feigh.RestService1Proxy;
import com.paem.feigh.RestService2Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("proxy")
public class ServicesProxy implements NameAwarness {

    @Autowired
    private RestService1Proxy proxy1;

    @Autowired
    private RestService2Proxy proxy2;

    public void callService1(Map<String, ExecutionResult> params) {
        ExecutionResult result = new ExecutionResult();
        result.setResult(proxy1.secure());
        result.setTaskHash(hashCode());
        result.setThreadName(Thread.currentThread().getName());
        String key = getName() + ":callService1";
        params.put(key, result);
    }

    public void callService2(Map<String, ExecutionResult> params) {
        ExecutionResult result = new ExecutionResult();
        result.setResult(proxy2.secure());
        result.setTaskHash(hashCode());
        result.setThreadName(Thread.currentThread().getName());
        String key = getName() + ":callService2";
        params.put(key, result);
    }

    @Override
    public String getName() {
        return "proxy";
    }
}
