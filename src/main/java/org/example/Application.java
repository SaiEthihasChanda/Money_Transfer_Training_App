package org.example;

import org.example.dtos.AccountDto;
import org.example.service.AccountService;
import org.example.service.AccountService;
import org.example.service.TransactionService;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "org.example")
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableJpaRepositories
public class Application {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger("Money-service");


    public static void main(String[] args) {

        //-----------------------------------------
        // Init / boot phase
        //-----------------------------------------
        logger.info("-".repeat(50));
        // based on configuration, initialize services, databases, etc.

        ConfigurableApplicationContext applicationContext = null;
        applicationContext = SpringApplication.run(Application.class, args);

        AccountService accountService = applicationContext.getBean(AccountService.class);
        TransactionService transactionService = applicationContext.getBean(TransactionService.class);

        //accountService.createAccount(new AccountDto("Alice", 1000));
        //accountService.createAccount(new AccountDto("Bob", 500));



        logger.info("-".repeat(50));

        //-----------------------------------------
        // Run phase
        //-----------------------------------------

        logger.info("-".repeat(50));
        //-----------------------------------------
        // Shutdown phase
        //-----------------------------------------
        logger.info("-".repeat(50));

    }
}
