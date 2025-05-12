package com.andrewbycode.cookdocs.repository;

import com.andrewbycode.cookdocs.entitys.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Long countByRecipeId(Long id);
    boolean existsByRecipeId(Long id);
    Optional<Like> findFirstByRecipeId(Long id);
}
