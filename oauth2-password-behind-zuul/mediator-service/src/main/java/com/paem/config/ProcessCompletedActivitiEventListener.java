package com.paem.config;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This listener is called when process is completed, additional check is done to make sure that
 * process that is completed is root process, not a child process
 */

@Component
public class ProcessCompletedActivitiEventListener implements ActivitiEventListener {

    @Autowired
    private ProcessScopeHolder processScopeHolder;

    @Override
    public void onEvent(ActivitiEvent event) {
        Object sourceEvent = ((ActivitiEntityEvent) event).getEntity();

        if (sourceEvent instanceof ProcessInstance && ((ProcessInstance) sourceEvent).getRootProcessInstanceId().equals(event.getExecutionId())) {
            // when process is finished remove custom scope bean from scope
            processScopeHolder.removeBeanFromScope(event.getProcessInstanceId());
        }

    }

    @Override
    public boolean isFailOnException() {
        return true;
    }


}
