package com.paem.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.springframework.security.oauth2.client.OAuth2ClientContext;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of the spring custom scope.
 * This scope in conjunction with {@link ActivitiEventListener} manages {@link OAuth2ClientContext} bean life cycle during process execution.
 */
public class ProcessScope implements Scope {

    private ConcurrentMap<String, OAuth2ClientContext> scopeBeanHolder = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Runnable> destructionCallbacks = new ConcurrentHashMap<>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        return scopeBeanHolder.computeIfAbsent(ProcessContext.getCurrentProcessInstanceId(), (pid) -> (OAuth2ClientContext) objectFactory.getObject());
    }

    @Override
    public Object remove(String name) {
        Runnable removeCallback = destructionCallbacks.remove(name);
        if (Objects.nonNull(removeCallback)) {
            removeCallback.run();
        }
        return scopeBeanHolder.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        destructionCallbacks.put(ProcessContext.getCurrentProcessInstanceId(), callback);
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        return "process";
    }
}
