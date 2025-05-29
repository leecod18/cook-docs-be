package com.andrewbycode.cookdocs.service.like;

public interface LikeService  {
    int likeRecipe(Long recipeId, String userName);
    Long getLikesCount(Long recipeId);
}
