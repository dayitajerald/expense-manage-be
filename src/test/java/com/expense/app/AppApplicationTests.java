package com.expense.app;

import com.expense.app.controller.ExpenseController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AppApplicationTests {

	@Autowired
	private ExpenseController controller;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}
