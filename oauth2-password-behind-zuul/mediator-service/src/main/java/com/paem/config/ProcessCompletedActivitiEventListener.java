package com.paem.config;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * This listener is called when process is completed, additional check is done to make sure that
 * process that is completed is root process, not a child process
 */

@Component
public class ProcessCompletedActivitiEventListener implements ActivitiEventListener, BeanFactoryAware, InitializingBean {

    private Scope processScope;

    @Override
    public void onEvent(ActivitiEvent event) {
        Object sourceEvent = ((ActivitiEntityEvent) event).getEntity();

        if (sourceEvent instanceof ProcessInstance && ((ProcessInstance) sourceEvent).getRootProcessInstanceId().equals(event.getExecutionId())) {
            // when process is finished remove custom scope bean from scope
            processScope.remove(event.getProcessInstanceId());
        }

    }

    @Override
    public boolean isFailOnException() {
        return true;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.processScope = ((ConfigurableBeanFactory) beanFactory).getRegisteredScope("process");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(processScope, "Process Scope should not be null");
    }
}
