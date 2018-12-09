package com.paem.config;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiProcessStartedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Listener is called when process is started, additional check is done to make sure that started process is root process
 */

@Component
public class ProcessStartedActivitiEventListener implements ActivitiEventListener {

    @Autowired
    private OAuth2ProcessTokenContext oAuth2ProcessTokenContext;

    @Override
    public void onEvent(ActivitiEvent event) {
        if (Objects.isNull(((ActivitiProcessStartedEvent) event).getNestedProcessInstanceId())) {
            ProcessContext.setCurrentProcessInstanceId(event.getProcessInstanceId());
            // call any method on proxy, so spring creates the actual bean for current process
            oAuth2ProcessTokenContext.getOAuth2AccessToken();
        }
    }

    @Override
    public boolean isFailOnException() {
        return true;
    }
}
