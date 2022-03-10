package me.rickychon.neo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ServerMain extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ServerMain.class, args);
    }
}
