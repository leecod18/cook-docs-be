package com.andrewbycode.cookdocs.service.review;

import com.andrewbycode.cookdocs.dto.ReviewDto;
import com.andrewbycode.cookdocs.entitys.Recipe;
import com.andrewbycode.cookdocs.entitys.Review;
import com.andrewbycode.cookdocs.entitys.User;
import com.andrewbycode.cookdocs.repository.RecipeRepository;
import com.andrewbycode.cookdocs.repository.ReviewRepository;
import com.andrewbycode.cookdocs.repository.UserRepository;
import com.andrewbycode.cookdocs.request.ReviewRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public void addReview(Long recipeId, ReviewRequest reviewRequest) {
        User user = userRepository.findById(reviewRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        boolean isUpdated = reviewRepository.findByRecipeIdAndUserId(recipeId, user.getId())
                .map(existingReview -> {
                    updateReview(existingReview, reviewRequest);
                    recipe.setAverageRating(recipe.calculateAverageRating());
                    return true;
                }).orElseGet(() -> {
                    createReview(recipe, reviewRequest);
                    return false;
                });
        if (!isUpdated) {
            recipe.setAverageRating(recipe.calculateAverageRating());
            recipe.setTotalRating(getTotalReviews(recipeId));
        }
        recipeRepository.save(recipe);
    }

    private void updateReview(Review review, ReviewRequest reviewRequest) {
        review.setStars(reviewRequest.getStars());
        review.setFeedBack(reviewRequest.getFeedBack());
        reviewRepository.save(review);
    }

    private void createReview(Recipe recipe, ReviewRequest reviewRequest) {
        User user = userRepository.findById(reviewRequest.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Review review = new Review();
        review.setUser(user);
        review.setStars(reviewRequest.getStars());
        review.setFeedBack(reviewRequest.getFeedBack());
        review.setRecipe(recipe);
        reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long recipeId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));

        recipe.getReviews().remove(review);
        reviewRepository.delete(review);

        recipe.setAverageRating(recipe.calculateAverageRating());
        recipe.setTotalRating(getTotalReviews(recipeId));
        recipeRepository.save(recipe);
    }

    @Override
    public int getTotalReviews(Long recipeId) {
        return reviewRepository.countAllByRecipeId(recipeId);
    }

    @Override
    public List<ReviewDto> findAllReviewByUserId(Long userId) {
        List<ReviewDto> listReview =  reviewRepository.findAllByUserId(userId)
                .stream()
                .map(review -> modelMapper.map(review,ReviewDto.class)).toList();
        if(listReview.isEmpty()){
            throw new EntityNotFoundException("Review not found");
        }
        return listReview;
    }
}
