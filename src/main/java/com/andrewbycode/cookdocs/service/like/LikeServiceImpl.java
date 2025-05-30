package com.andrewbycode.cookdocs.service.like;

import com.andrewbycode.cookdocs.entitys.Like;
import com.andrewbycode.cookdocs.entitys.User;
import com.andrewbycode.cookdocs.repository.LikeRepository;
import com.andrewbycode.cookdocs.repository.RecipeRepository;
import com.andrewbycode.cookdocs.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final RecipeRepository recipeRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public int likeRecipe(Long recipeId, String userName) {
        User user = userRepository.findByUserName(userName);
        return recipeRepository.findById(recipeId).map(recipe -> {
            if (!likeRepository.existsByRecipeIdAndUserId(recipeId, user.getId())) {
                Like like = new Like();
                like.setRecipe(recipe);
                like.setUser(user);
                likeRepository.save(like);
                recipe.setLikeCount(recipe.getLikeCount() + 1);
            } else {
                Like liked = likeRepository.findByRecipeIdAndUserId(recipeId, user.getId());
                recipe.setLikeCount(recipe.getLikeCount() - 1);
                likeRepository.delete(liked);
            }
            return recipeRepository.save(recipe).getLikeCount();
        }).orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
    }


    @Override
    public Long getLikesCount(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .map(recipe -> likeRepository.countByRecipeId(recipe.getId())).orElse(0L);
    }

    @Override
    public Like getLikeByRecipeIdAndUserId(Long recipeId, Long userId) {
        Like like = likeRepository.findByRecipeIdAndUserId(recipeId, userId);
        if (like == null) {
            throw new EntityNotFoundException("Like not found for recipeId: " + recipeId + " and userId: " + userId);
        }
        return like;
    }
}
