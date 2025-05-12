package com.andrewbycode.cookdocs.controller;

import com.andrewbycode.cookdocs.dto.RecipeDto;
import com.andrewbycode.cookdocs.request.CreateRecipeRequest;
import com.andrewbycode.cookdocs.request.RecipeUpdateRequest;
import com.andrewbycode.cookdocs.service.recipe.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
@CrossOrigin
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<RecipeDto> createRecipe(@RequestBody CreateRecipeRequest request) {
        return ResponseEntity.ok(recipeService.convertToDto(recipeService.createRecipe(request)));
    }

    @GetMapping
    public ResponseEntity<List<RecipeDto>> getAllRecipes() {
       return ResponseEntity.ok(recipeService.getConvertedRecipes(recipeService.getALLRecipes()));
    }

    @GetMapping("/{id}/recipe")
    public ResponseEntity<RecipeDto> getRecipeById(@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.convertToDto(recipeService.getRecipeById(id)) );
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<RecipeDto> updateRecipeById(@RequestBody RecipeUpdateRequest request,@PathVariable Long id) {
        return ResponseEntity.ok(recipeService.convertToDto(recipeService.updateRecipeById(request, id))) ;
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteRecipeById(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    public ResponseEntity<Set<String>> getAllRecipeCategories() {
        return ResponseEntity.ok(recipeService.getALlRecipeCategories());
    }

    @GetMapping("/cuisines")
    public ResponseEntity<Set<String>> getAllRecipeCuisines() {
        return ResponseEntity.ok(recipeService.getAllRecipeCuisines());
    }


}
