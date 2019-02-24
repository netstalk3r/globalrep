package com.paem;


import com.paem.work.Memory;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SpringBootApplication(exclude = { org.activiti.spring.boot.SecurityAutoConfiguration.class})
@EnableEurekaClient
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Mediator {
    public static void main(String[] args) {
        SpringApplication.run(Mediator.class, args);
    }

    @RestController
    @RequestMapping("/")
    public static class Api {

        @Autowired
        private RuntimeService runtimeService;

        @Autowired
        private RepositoryService repositoryService;

        @Autowired
        private Memory memory;


        @PostMapping("/start")
        @PreAuthorize("#oauth2.hasScope('write') and hasRole('ROLE_ADMIN')")
        public String startProcess() {
            Map<String, Execution> params = new ConcurrentHashMap<>();
            ProcessInstance testProcess = runtimeService.startProcessInstanceByKey("TestProcess", Collections.singletonMap("params", params));
            return testProcess.getId();
        }

        @GetMapping("/values")
        public String getValues(@RequestParam("id") String executionId) {
            return String.valueOf(memory.get(executionId));
        }

        @GetMapping("/query")
        public String queryProcessCount() {
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
            return processDefinitions.stream().map(pd -> pd.getId() + "~" + pd.getName()).collect(Collectors.joining(";"));
        }
    }

}
