package com.rev.app.config;

import com.rev.app.entity.User;
import com.rev.app.entity.User.Role;
import com.rev.app.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AdminDataLoader implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminDataLoader(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("admin@revpay.com").isEmpty()) {
            User admin = new User();
            admin.setFullName("System Administrator");
            admin.setEmail("admin@revpay.com");
            admin.setPhoneNumber("0000000000");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setSecurityQuestion("Admin Key?");
            admin.setSecurityAnswer("admin");
            admin.setTransactionAlerts(true);
            admin.setSecurityAlerts(true);
            
            userRepository.save(admin);
            log.info("Default Admin user created successfully (admin@revpay.com / admin123)");
        } else {
            log.info("Admin user already exists.");
        }
    }
}
