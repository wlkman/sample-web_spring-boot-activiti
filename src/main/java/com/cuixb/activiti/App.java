package com.cuixb.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = org.activiti.spring.boot.SecurityAutoConfiguration.class)
public class App 
{
	 public static void main(String[] args) {
	        SpringApplication.run(App.class, args);
	 }
	 
    @Bean
    public CommandLineRunner init(final RepositoryService repositoryService,
                                  final RuntimeService runtimeService,
                                  final TaskService taskService) {

        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                System.out.println("Number of process definitions : "+ repositoryService.createProcessDefinitionQuery().count());
        
            }
        };

    }
	
}
