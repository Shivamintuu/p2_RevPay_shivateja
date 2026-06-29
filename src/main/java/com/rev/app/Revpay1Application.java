package com.rev.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
public class Revpay1Application {

    public static void main(String[] args) {
        SpringApplication.run(Revpay1Application.class, args);
    }

    @Bean
    public CommandLineRunner logSequences(DataSource dataSource) {
        return args -> {
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT sequence_name FROM user_sequences")) {
                System.out.println("--- DATABASE SEQUENCES ---");
                while (rs.next()) {
                    System.out.println("Sequence Found: " + rs.getString("sequence_name"));
                }
                System.out.println("--------------------------");
            } catch (Exception e) {
                System.err.println("Failed to query sequences: " + e.getMessage());
            }
        };
    }

}
