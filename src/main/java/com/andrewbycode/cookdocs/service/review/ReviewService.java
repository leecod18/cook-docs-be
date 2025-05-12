package com.andrewbycode.cookdocs.service.review;

import com.andrewbycode.cookdocs.request.ReviewRequest;

public interface ReviewService {
    void addReview(Long recipeId, ReviewRequest review);
    void deleteReview(Long recipeId, Long reviewId);
    int getTotalReviews(Long recipeId);

}
