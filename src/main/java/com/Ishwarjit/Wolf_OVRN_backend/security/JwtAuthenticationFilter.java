package com.Ishwarjit.Wolf_OVRN_backend.security;

import com.Ishwarjit.Wolf_OVRN_backend.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String COOKIE_NAME = "auth_token";

    private final JwtService jwtService;
    private final boolean cookieSecure;
    private final String cookieDomain;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            @org.springframework.beans.factory.annotation.Value("${app.cookie.secure}") boolean cookieSecure,
            @org.springframework.beans.factory.annotation.Value("${app.cookie.domain:}") String cookieDomain) {
        this.jwtService = jwtService;
        this.cookieSecure = cookieSecure;
        this.cookieDomain = cookieDomain;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Claims claims = jwtService.parse(token);
                String type = claims.get("type", String.class);
                if (type != null && !"access".equals(type)) {
                    throw new JwtException("Invalid token type");
                }
                String userId = claims.getSubject();
                String role = claims.get("role", String.class);

                List<SimpleGrantedAuthority> authorities = role == null
                        ? List.of()
                        : List.of(new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException | IllegalArgumentException ex) {
                SecurityContextHolder.clearContext();
                Cookie cookie = new Cookie(COOKIE_NAME, "");
                cookie.setHttpOnly(true);
                cookie.setSecure(cookieSecure);
                cookie.setPath("/");
                if (cookieDomain != null && !cookieDomain.isBlank()) {
                    cookie.setDomain(cookieDomain);
                }
                cookie.setMaxAge(0);
                cookie.setAttribute("SameSite", "Lax");
                response.addCookie(cookie);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
