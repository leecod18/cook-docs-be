package com.andrewbycode.cookdocs.repository;

import com.andrewbycode.cookdocs.entitys.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Long countByRecipeId(Long id);
    boolean existsByRecipeIdAndUserId(Long recipeId, Long userId);
    Like findByRecipeIdAndUserId(Long recipeId, Long userId);
}
