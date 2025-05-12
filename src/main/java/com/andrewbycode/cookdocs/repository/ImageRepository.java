package com.andrewbycode.cookdocs.repository;

import com.andrewbycode.cookdocs.entitys.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByRecipeId(Long recipeId);
    Image findByUserIdAndRecipeId(Long userId, Long recipeId);
}
