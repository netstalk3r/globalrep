package com.paem;

import com.cmlatitude.feign.proxy.RestService1Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class SchedulerConfiguration {

    public class SchedulingWork {

        @Autowired
        private RestService1Proxy restService1Proxy;

        @Scheduled(fixedDelay = 30000)
        public void doWork() {
            try {
                System.out.println(restService1Proxy.secure());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Bean
    public SchedulingWork schedulingWork() {
        return new SchedulingWork();
    }

}
