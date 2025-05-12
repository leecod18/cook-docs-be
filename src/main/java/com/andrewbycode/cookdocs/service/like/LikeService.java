package com.andrewbycode.cookdocs.service.like;

public interface LikeService  {
    int likeRecipe(Long recipeId);
    int decrementLikeCount(Long recipeId);
    Long getLikesCount(Long recipeId);
}
