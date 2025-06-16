package com.andrewbycode.cookdocs.controller;

import com.andrewbycode.cookdocs.dto.ReviewDto;
import com.andrewbycode.cookdocs.request.ReviewRequest;
import com.andrewbycode.cookdocs.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/recipe/{recipeId}/review")
    public ResponseEntity<Void> rateReview(@RequestBody ReviewRequest request, @PathVariable Long recipeId) {
        reviewService.addReview(recipeId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recipe/{recipeId}/total-rating")
    public ResponseEntity<Integer> getTotalReviews(@PathVariable Long recipeId) {
        return ResponseEntity.ok(reviewService.getTotalReviews(recipeId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.findAllReviewByUserId(userId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReview(@RequestParam Long reviewId, @RequestParam Long recipeId) {
        reviewService.deleteReview(recipeId, reviewId);
        return ResponseEntity.ok("Review deleted successfully");
    }
}
