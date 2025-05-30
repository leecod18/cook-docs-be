package com.andrewbycode.cookdocs.service.like;

import com.andrewbycode.cookdocs.entitys.Like;

public interface LikeService  {
    int likeRecipe(Long recipeId, String userName);
    Long getLikesCount(Long recipeId);
    Like getLikeByRecipeIdAndUserId(Long recipeId, Long userId);
}
