package com.andrewbycode.cookdocs.repository;

import com.andrewbycode.cookdocs.entitys.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    int countAllByRecipeId(Long id);
    Optional<Review> findByRecipeIdAndUserId(Long recipeId,Long userId);
    Review findByRecipeId(Long id);
    List<Review> findAllByRecipeId(Long id);
}
