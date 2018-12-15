package com.paem.config;

import org.activiti.engine.impl.cmd.JobRetryCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.JobEntity;

/**
 * When activiti job is failed, it is retried three times by default.
 * This is is to prevent this retry.
 * Did not find another fast approach
 */
public class JobNoRetryCmd extends JobRetryCmd {

    public JobNoRetryCmd(String jobId, Throwable exception) {
        super(jobId, exception);
    }

    @Override
    public Object execute(CommandContext commandContext) {
        JobEntity job = commandContext.getJobEntityManager().findById(jobId);
        if (job == null) {
            return null;
        } else {
            job.setRetries(0);
        }
        return super.execute(commandContext);
    }
}
