package com.andrewbycode.cookdocs.repository;

import com.andrewbycode.cookdocs.entitys.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    User findByEmailAndClientRegistration(String email, int clientRes);
    Long id(Long id);
}
