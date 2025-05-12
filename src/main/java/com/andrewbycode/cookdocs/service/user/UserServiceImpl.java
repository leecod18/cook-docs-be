package com.andrewbycode.cookdocs.service.user;

import com.andrewbycode.cookdocs.dto.JwtResponse;
import com.andrewbycode.cookdocs.dto.UserDto;
import com.andrewbycode.cookdocs.entitys.User;
import com.andrewbycode.cookdocs.enums.ClientRegistrationEnum;
import com.andrewbycode.cookdocs.enums.CookNameEnum;
import com.andrewbycode.cookdocs.login.CustomUserDetails;
import com.andrewbycode.cookdocs.repository.UserRepository;
import com.andrewbycode.cookdocs.utils.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse registerUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {

        if (oAuth2AuthenticationToken == null) {
            throw new AuthenticationCredentialsNotFoundException("OAuth2 authentication is required");
        }

        String email = oAuth2AuthenticationToken.getPrincipal().getAttribute("email");
        int clientRes = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals(ClientRegistrationEnum.FACEBOOK.getValue())
                ? ClientRegistrationEnum.FACEBOOK.getCode() : ClientRegistrationEnum.GOOGLE.getCode();
        User user = userRepository.findByEmailAndClientRegistration(email, clientRes);
        if (user == null) {
            user = new User();
            user.setUserName(generateUsername());
            user.setEmail(email);
            user.setClientRegistration(clientRes);
            userRepository.save(user);
        }
        User userDetails = new CustomUserDetails(user).getUser();
        String jwt = jwtUtils.generateToken(userDetails);
        return new JwtResponse(jwt, modelMapper.map(user, UserDto.class));
    }

    private String generateUsername() {
        return CookNameEnum.CHEF_CODE.getMsg() + UUID.randomUUID().toString().substring(0, 5);
    }

    @Override
    public UserDto findUserByUsername(String username) {
        User user = userRepository.findByUserName((username));
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(String username, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setUserName(username);
        userRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUserName(username);
    }
}
