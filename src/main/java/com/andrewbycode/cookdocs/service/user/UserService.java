package com.andrewbycode.cookdocs.service.user;

import com.andrewbycode.cookdocs.dto.JwtResponse;
import com.andrewbycode.cookdocs.dto.UserDto;
import com.andrewbycode.cookdocs.entitys.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public interface UserService {
    JwtResponse registerUser(OAuth2AuthenticationToken oAuth2AuthenticationToken);
    UserDto findUserByUsername(String username);
    UserDto updateUser(String username, Long userId);
    User getUserById(Long userId);
    User findByUsername(String username);
}
