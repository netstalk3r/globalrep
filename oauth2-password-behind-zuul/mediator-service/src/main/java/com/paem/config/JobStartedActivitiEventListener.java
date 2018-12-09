package com.paem.config;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.springframework.stereotype.Component;

/**
 * This listener is called when in new thread from thread pool activiti work is submitted
 */

@Component
public class JobStartedActivitiEventListener implements ActivitiEventListener {

    @Override
    public void onEvent(ActivitiEvent event) {
        ProcessContext.setCurrentProcessInstanceId(event.getProcessInstanceId());
    }

    @Override
    public boolean isFailOnException() {
        return true;
    }
}
