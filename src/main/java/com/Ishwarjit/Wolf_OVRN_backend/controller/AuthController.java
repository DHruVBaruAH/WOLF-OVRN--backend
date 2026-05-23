package com.Ishwarjit.Wolf_OVRN_backend.controller;

import com.Ishwarjit.Wolf_OVRN_backend.dto.ApiResponse;
import com.Ishwarjit.Wolf_OVRN_backend.dto.UserResponse;
import com.Ishwarjit.Wolf_OVRN_backend.exception.UnauthorizedException;
import com.Ishwarjit.Wolf_OVRN_backend.repository.UserRepository;
import com.Ishwarjit.Wolf_OVRN_backend.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.Ishwarjit.Wolf_OVRN_backend.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final boolean cookieSecure;
    private final String cookieDomain;

    public AuthController(
            UserRepository userRepository,
            JwtService jwtService,
            @Value("${app.cookie.secure}") boolean cookieSecure,
            @Value("${app.cookie.domain:}") String cookieDomain) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.cookieSecure = cookieSecure;
        this.cookieDomain = cookieDomain;
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(JwtAuthenticationFilter.COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        if (cookieDomain != null && !cookieDomain.isBlank()) {
            cookie.setDomain(cookieDomain);
        }
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);

        Cookie refreshCookie = new Cookie("refresh_token", "");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(cookieSecure);
        refreshCookie.setPath("/api/auth/refresh");
        if (cookieDomain != null && !cookieDomain.isBlank()) {
            refreshCookie.setDomain(cookieDomain);
        }
        refreshCookie.setMaxAge(0);
        refreshCookie.setAttribute("SameSite", "Lax");
        response.addCookie(refreshCookie);

        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.ok(null, "Logged out successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Void>> refresh(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new UnauthorizedException("No refresh token provided");
        }

        try {
            Claims claims = jwtService.parse(refreshToken);
            String type = claims.get("type", String.class);
            if (!"refresh".equals(type)) {
                throw new UnauthorizedException("Invalid token type");
            }
            
            String userIdStr = claims.getSubject();
            UUID userId = UUID.fromString(userIdStr);
            com.Ishwarjit.Wolf_OVRN_backend.entity.User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UnauthorizedException("User not found"));

            String newAccessToken = jwtService.generateToken(user);

            Cookie cookie = new Cookie(JwtAuthenticationFilter.COOKIE_NAME, newAccessToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(cookieSecure);
            cookie.setPath("/");
            if (cookieDomain != null && !cookieDomain.isBlank()) {
                cookie.setDomain(cookieDomain);
            }
            cookie.setMaxAge((int) (jwtService.getExpirationMs() / 1000));
            cookie.setAttribute("SameSite", "Lax");
            response.addCookie(cookie);

            return ResponseEntity.ok(ApiResponse.ok(null, "Token refreshed successfully"));
        } catch (JwtException | IllegalArgumentException ex) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UnauthorizedException("Not authenticated");
        }

        Object principal = authentication.getPrincipal();
        UUID userId;
        try {
            userId = UUID.fromString(principal.toString());
        } catch (IllegalArgumentException ex) {
            throw new UnauthorizedException("Invalid authentication principal");
        }

        UserResponse user = userRepository.findById(userId)
                .map(UserResponse::from)
                .orElseThrow(() -> new UnauthorizedException("Authenticated user not found"));
        return ResponseEntity.ok(ApiResponse.ok(user));
    }
}
