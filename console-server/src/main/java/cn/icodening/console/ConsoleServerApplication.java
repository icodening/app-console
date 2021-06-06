package cn.icodening.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * @author icodening
 * @date 2021.05.24
 */
@SpringBootApplication
@EntityScan(basePackages = "cn.icodening.console.entity")
public class ConsoleServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsoleServerApplication.class);
    }
}
