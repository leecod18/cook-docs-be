package com.andrewbycode.cookdocs.repository;

import com.andrewbycode.cookdocs.entitys.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RecipeRepository  extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByUserId(Long UserId);
}
