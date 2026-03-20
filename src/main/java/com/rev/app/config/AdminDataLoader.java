package com.rev.app.config;

import com.rev.app.entity.User;
import com.rev.app.entity.User.Role;
import com.rev.app.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.rev.app.service.IWalletService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AdminDataLoader implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IWalletService walletService;

    @org.springframework.beans.factory.annotation.Value("${app.admin.email}")
    private String adminEmail;

    @org.springframework.beans.factory.annotation.Value("${app.admin.password}")
    private String adminPassword;

    @org.springframework.beans.factory.annotation.Value("${app.admin.security-answer}")
    private String adminSecurityAnswer;

    @Autowired
    public AdminDataLoader(IUserRepository userRepository, PasswordEncoder passwordEncoder, IWalletService walletService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.walletService = walletService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setFullName("System Administrator");
            admin.setEmail(adminEmail);
            admin.setPhoneNumber("0000000000");
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            admin.setSecurityQuestion("Admin Key?");
            admin.setSecurityAnswer(adminSecurityAnswer);
            admin.setTransactionAlerts(true);
            admin.setSecurityAlerts(true);
            
            // Give Admin a default Transaction PIN equal to login password
            admin.setTransactionPin(passwordEncoder.encode(adminPassword));
            
            User savedAdmin = userRepository.save(admin);
            walletService.createWallet(savedAdmin.getId());
            
            log.info("Default Admin user created successfully ({})", adminEmail);
        } else {
            log.info("Admin user already exists.");
        }
    }
}
