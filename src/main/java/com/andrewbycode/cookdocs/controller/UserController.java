package com.andrewbycode.cookdocs.controller;

import com.andrewbycode.cookdocs.dto.JwtResponse;
import com.andrewbycode.cookdocs.dto.UserDto;
import com.andrewbycode.cookdocs.entitys.User;
import com.andrewbycode.cookdocs.service.user.UserService;
import com.andrewbycode.cookdocs.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        // Extract JWT from cookies
        String jwt = extractJwtFromCookies(request);

        // Check if JWT is present and valid
        if (jwt == null || !jwtUtils.validateToken(jwt)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        try {
            // Extract username and find user
            String username = jwtUtils.getUsernameFromToken(jwt);
            User user = userService.findByUsername(username);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // Log the error (in a real application)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request");
        }
    }

    private String extractJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> "jwt".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    @GetMapping("/register")
    public ResponseEntity<JwtResponse> registerUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return ResponseEntity.ok(userService.registerUser(oAuth2AuthenticationToken));
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // Xoá session
        request.getSession().invalidate();

        // Xoá JSESSIONID (tuỳ thuộc vào bạn có dùng session hay không)
        Cookie sessionCookie = new Cookie("JSESSIONID", null);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(0); // Xoá cookie
        response.addCookie(sessionCookie);

        // Xoá JWT cookie
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(0); // Xoá cookie
        response.addCookie(jwtCookie);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUsername(@RequestParam String username, @PathVariable Long userId) {
        return ResponseEntity.ok(userService.updateUser(username, userId));
    }

    @GetMapping
    public ResponseEntity<UserDto> findUserByUserName(@RequestParam String userName) {
        return ResponseEntity.ok(userService.findUserByUsername(userName));
    }
}
