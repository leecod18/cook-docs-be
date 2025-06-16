package com.andrewbycode.cookdocs.service.recipe;

import com.andrewbycode.cookdocs.dto.RecipeDto;
import com.andrewbycode.cookdocs.entitys.Recipe;
import com.andrewbycode.cookdocs.request.CreateRecipeRequest;
import com.andrewbycode.cookdocs.request.RecipeUpdateRequest;

import java.util.List;
import java.util.Set;

public interface RecipeService {
    Recipe createRecipe(CreateRecipeRequest request);

    List<Recipe> getALLRecipes();

    Recipe getRecipeById(Long id);

    Recipe updateRecipeById(RecipeUpdateRequest request, Long recipeId);

    void deleteRecipe(Long recipeId);

    Set<String> getALlRecipeCategories();

    Set<String> getAllRecipeCuisines();

    List<RecipeDto> getConvertedRecipes(List<Recipe> recipes);

    RecipeDto convertToDto(Recipe recipe);

    List<RecipeDto> findAllByRecipeId(Long recipeId);
}
