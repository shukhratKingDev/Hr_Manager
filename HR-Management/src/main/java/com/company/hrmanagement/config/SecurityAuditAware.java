package com.company.hrmanagement.config;

import com.company.hrmanagement.entity.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class SecurityAuditAware implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null&&authentication.isAuthenticated()
                && !authentication.getPrincipal().equals("anonymousUser")
        ) {
            User user=(User)authentication.getPrincipal();
            return Optional.of(user.getId());
        }
        return Optional.empty();
    }
}
