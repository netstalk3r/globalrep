package com.paem.config;

import org.activiti.engine.ManagementService;
import org.activiti.engine.runtime.DeadLetterJobQuery;
import org.activiti.engine.runtime.Job;
import org.springframework.beans.factory.config.Scope;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class ProcessScopeHolder {

    private Scope processScope;

    private ManagementService managementService;

    private Instant dueDate;

    public ProcessScopeHolder(Scope processScope, ManagementService managementService) {
        this.processScope = processScope;
        this.managementService = managementService;
    }

    public void removeBeanFromScope(String beanIdentifier) {
        processScope.remove(beanIdentifier);
    }

    @Scheduled(fixedDelay = 30000)
    public void cleanUpOddBeans() {
        Instant now = Instant.now();
        DeadLetterJobQuery deadLetterJobQuery = managementService.createDeadLetterJobQuery().withException();
        if (Objects.nonNull(dueDate)) {
            deadLetterJobQuery.duedateHigherThan(Date.from(dueDate));
        }
        List<Job> list = deadLetterJobQuery.list();
        System.out.println("Bean to clean up " + list.size());
        list.stream().peek(job -> System.out.println("scheduled remove + " + job.getProcessInstanceId())).forEach(job -> removeBeanFromScope(job.getProcessInstanceId()));
        dueDate = now;
    }
}
