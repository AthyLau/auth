package com.bizseer.auth.config;

import com.bizseer.auth.service.AuthService;
import com.bizseer.auth.util.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


@ConditionalOnProperty(name = "security.auth.enable", havingValue = "true")
@Configuration
@Order(1)
@Slf4j
public class StartupConfig implements CommandLineRunner {

    @Autowired
    private AuthService loginService;

    @Autowired
    private Config config;

    @Value("${security.auth.enable}")
    public boolean authEnable;

    @Override
    public void run(String... args) {
        if (authEnable && loginService.refreshAuthenticator(config)) {
            log.debug("auth driver refreshed");
        }
    }
}
