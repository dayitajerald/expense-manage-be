package com.expense.app;

import com.expense.app.prerequisites.PopulateTables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableScheduling
public class AppApplication implements ApplicationRunner {
	@Autowired
	PopulateTables preRequisites;

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);

	}
	@Override
	public void run(ApplicationArguments args) throws Exception {
		preRequisites.initialize();
	}

}
