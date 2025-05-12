package com.andrewbycode.cookdocs.service.like;

import com.andrewbycode.cookdocs.entitys.Like;
import com.andrewbycode.cookdocs.entitys.Recipe;
import com.andrewbycode.cookdocs.repository.LikeRepository;
import com.andrewbycode.cookdocs.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final RecipeRepository recipeRepository;
    private final LikeRepository likeRepository;

    @Override
    public int likeRecipe(Long recipeId) {
        return recipeRepository.findById(recipeId).map(recipe -> {
            if (!likeRepository.existsByRecipeId(recipe.getId())) {
                Like like = new Like();
                likeRepository.save(like);
            }
            recipe.setLikeCount(recipe.getLikeCount() + 1);
            return recipeRepository.save(recipe).getLikeCount();
        }).orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
    }

    @Override
    @Transactional
    public int decrementLikeCount(Long recipeId) {
        return likeRepository.findFirstByRecipeId(recipeId).map(like -> {
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow();
            if (recipe.getLikeCount() > 0) {
                recipe.setLikeCount(recipe.getLikeCount() - 1);
                recipeRepository.save(recipe);
            } else {
                throw new IllegalArgumentException("Like is already zero");
            }
            return recipe.getLikeCount();
        }).orElseThrow(() -> new EntityNotFoundException("No likes found for this recipe"));
    }

    @Override
    public Long getLikesCount(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .map(recipe -> likeRepository.countByRecipeId(recipe.getId())).orElse(0L);
    }
}
