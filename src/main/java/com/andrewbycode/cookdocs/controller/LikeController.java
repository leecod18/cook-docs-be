package com.andrewbycode.cookdocs.controller;

import com.andrewbycode.cookdocs.dto.LikeResponseDto;
import com.andrewbycode.cookdocs.entitys.Like;
import com.andrewbycode.cookdocs.service.like.LikeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/recipe/{recipeId}/like/{userName}")
    public ResponseEntity<Integer> likeRecipe(@PathVariable Long recipeId, @PathVariable String userName) {
        int like = likeService.likeRecipe(recipeId, userName);
        System.out.println(like);
        return ResponseEntity.ok(like);
    }
    @GetMapping
    public ResponseEntity<?> getLikeByRecipeIdAndUserId(
            @RequestParam("recipeId") Long recipeId,
            @RequestParam("userId") Long userId) {

        System.out.println("RECEIVED: recipeId=" + recipeId + ", userId=" + userId);

        try {
            Like like = likeService.getLikeByRecipeIdAndUserId(recipeId, userId);

            LikeResponseDto responseDto = new LikeResponseDto();
            responseDto.setId(like.getId());
            responseDto.setRecipeId(like.getRecipe().getId());       // assuming Like has getRecipe()
            responseDto.setUsername(like.getUser().getUserName());   // assuming Like has getUser()

            return ResponseEntity.ok(responseDto);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/recipe/{recipeId}/like-count")
    public ResponseEntity<Long> getLikesCount(@PathVariable Long recipeId) {
        Long likesCount = likeService.getLikesCount(recipeId);
        return ResponseEntity.ok(likesCount);
    }

}
