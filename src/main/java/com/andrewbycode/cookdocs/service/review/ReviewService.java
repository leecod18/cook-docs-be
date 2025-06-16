package com.andrewbycode.cookdocs.service.review;

import com.andrewbycode.cookdocs.dto.ReviewDto;
import com.andrewbycode.cookdocs.request.ReviewRequest;

import java.util.List;

public interface ReviewService {
    void addReview(Long recipeId, ReviewRequest review);
    void deleteReview(Long recipeId, Long reviewId);
    int getTotalReviews(Long recipeId);
    List<ReviewDto> findAllReviewByUserId(Long userId);

}
