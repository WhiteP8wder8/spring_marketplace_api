package com.api.spring_marketplace_api.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class AdminUtil {

    private AdminUtil() {}

    public static boolean isAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }

        return auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

}
