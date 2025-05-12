package com.andrewbycode.cookdocs.controller;

import com.andrewbycode.cookdocs.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/recipe/{recipeId}/like")
    public ResponseEntity<Integer> likeRecipe(@PathVariable Long recipeId) {
        int like = likeService.likeRecipe(recipeId);
        return ResponseEntity.ok(like);
    }

    @PutMapping("/recipe/{recipeId}/decrement-like")
    public ResponseEntity<Integer> updateLikeRecipe(@PathVariable Long recipeId) {
        int like = likeService.decrementLikeCount(recipeId);
        return ResponseEntity.ok(like);
    }

    @GetMapping("/recipe/{recipeId}/like-count")
    public ResponseEntity<Long> getLikesCount(@PathVariable Long recipeId) {
        Long likesCount = likeService.getLikesCount(recipeId);
        return ResponseEntity.ok(likesCount);
    }

}
