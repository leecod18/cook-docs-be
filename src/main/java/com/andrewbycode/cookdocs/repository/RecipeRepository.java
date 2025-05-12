package com.andrewbycode.cookdocs.repository;

import com.andrewbycode.cookdocs.entitys.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecipeRepository  extends JpaRepository<Recipe, Long> {
}
