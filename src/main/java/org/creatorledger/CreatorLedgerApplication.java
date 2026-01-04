package org.creatorledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CreatorLedgerApplication {

    public static void main(final String[] args) {
        SpringApplication.run(CreatorLedgerApplication.class, args);
    }

}
