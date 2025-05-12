package com.andrewbycode.cookdocs.config;

import com.andrewbycode.cookdocs.login.OAuth2LoginSuccessHandler;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class AppConfig {
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    public AppConfig(@Lazy OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom Converter: Integer -> String cho imageType
        Converter<Integer, String> imageTypeConverter = context -> {
            if (context.getSource() == null) return null;
            return context.getSource() == 0 ? "Ảnh hồ sơ" : "Ảnh món ăn";
        };

        Converter<Integer, String> clientRegistrationConverter = context -> {
            if (context.getSource() == null) return null;
            return context.getSource() == 0 ? "facebook" : "google";
        };

        // Thêm Converter vào ModelMapper
        modelMapper.typeMap(com.andrewbycode.cookdocs.entitys.Image.class, com.andrewbycode.cookdocs.dto.ImageDto.class)
                .addMappings(mapper -> mapper.using(imageTypeConverter)
                        .map(com.andrewbycode.cookdocs.entitys.Image::getImageType, com.andrewbycode.cookdocs.dto.ImageDto::setImageType));

        modelMapper.typeMap(com.andrewbycode.cookdocs.entitys.User.class, com.andrewbycode.cookdocs.dto.UserDto.class)
                .addMappings(mapper -> mapper.using(clientRegistrationConverter)
                        .map(com.andrewbycode.cookdocs.entitys.User::getClientRegistration, com.andrewbycode.cookdocs.dto.UserDto::setClientRegistration));


        return modelMapper;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/api/**", "/login").permitAll()
                            .anyRequest().authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 ->
                        oauth2.successHandler(oAuth2LoginSuccessHandler))
                .build();

    }

}
