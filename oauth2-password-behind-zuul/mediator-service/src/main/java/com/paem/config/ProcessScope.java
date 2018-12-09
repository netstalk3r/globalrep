package com.paem.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.activiti.engine.delegate.event.ActivitiEventListener;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the spring custom scope.
 * This scope in conjunction with {@link ActivitiEventListener} manages {@link OAuth2ProcessTokenContext} bean life cycle during process execution.
 */
public class ProcessScope implements Scope {

    private ConcurrentHashMap<String, OAuth2ProcessTokenContext> scopeBeanHolder = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Runnable> destructionCallbacks = new ConcurrentHashMap<>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        return scopeBeanHolder.computeIfAbsent(ProcessContext.getCurrentProcessInstanceId(), (pid) -> (OAuth2ProcessTokenContext) objectFactory.getObject());
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
