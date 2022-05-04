package br.com.sicredi.sicrediresilience4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableFeignClients
@EnableMongoRepositories
public class SicrediResilience4jApplication {

    public static void main(String[] args) {
        SpringApplication.run(SicrediResilience4jApplication.class, args);
    }

}