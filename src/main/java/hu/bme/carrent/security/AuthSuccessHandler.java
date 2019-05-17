package hu.bme.carrent.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String roles = auth.getAuthorities().toString();

        String targetUrl = "";
        if (roles.contains("user") || roles.contains("USER")) {
            targetUrl = "/userpage";
        } else if (roles.contains("owner") ||roles.contains("OWNER")) {
            targetUrl = "/ownerpage";
        }
        return targetUrl;
    }
}
