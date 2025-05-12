package com.andrewbycode.cookdocs.service.recipe;

import com.andrewbycode.cookdocs.dto.ImageDto;
import com.andrewbycode.cookdocs.dto.RecipeDto;
import com.andrewbycode.cookdocs.dto.ReviewDto;
import com.andrewbycode.cookdocs.dto.UserDto;
import com.andrewbycode.cookdocs.entitys.Image;
import com.andrewbycode.cookdocs.entitys.Recipe;
import com.andrewbycode.cookdocs.entitys.User;
import com.andrewbycode.cookdocs.repository.ImageRepository;
import com.andrewbycode.cookdocs.repository.RecipeRepository;
import com.andrewbycode.cookdocs.repository.ReviewRepository;
import com.andrewbycode.cookdocs.repository.UserRepository;
import com.andrewbycode.cookdocs.request.CreateRecipeRequest;
import com.andrewbycode.cookdocs.request.RecipeUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RecipeServiceImpl  implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public Recipe createRecipe(CreateRecipeRequest request) {
        if(request == null) {
           throw new IllegalArgumentException("Invalid request");
       }
        Recipe recipe = new Recipe();
        Recipe createRequest = request.getRecipe();
        recipe.setTitle(createRequest.getTitle());
        recipe.setCuisine(createRequest.getCuisine());
        recipe.setCategory(createRequest.getCategory());
        recipe.setInstruction(createRequest.getInstruction());
        recipe.setDescription(createRequest.getDescription());
        recipe.setPrepTime(createRequest.getPrepTime());
        recipe.setCookTime(createRequest.getCookTime());
        recipe.setIngredients(createRequest.getIngredients());

        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        recipe.setUser(user);

        return recipeRepository.save(recipe) ;
    }

    @Override
    public Recipe updateRecipeById(RecipeUpdateRequest request, Long recipeId) {
        Recipe existingRecipe = getRecipeById(recipeId);
        if(existingRecipe == null) {
            throw new EntityNotFoundException("Recipe not found");
        }
        existingRecipe.setTitle(request.getTitle());
        existingRecipe.setCuisine(request.getCuisine());
        existingRecipe.setCategory(request.getCategory());
        existingRecipe.setInstruction(request.getInstruction());
        existingRecipe.setDescription(request.getDescription());
        existingRecipe.setPrepTime(request.getPrepTime());
        existingRecipe.setCookTime(request.getCookTime());
        existingRecipe.setIngredients(request.getIngredients());
        return recipeRepository.save(existingRecipe);
    }

    @Override
    public List<Recipe> getALLRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
    }

    @Override
    public void deleteRecipe(Long recipeId) {
        getRecipeById(recipeId);
        recipeRepository.deleteById(recipeId);
    }

    @Override
    public Set<String> getALlRecipeCategories() {
        return recipeRepository.findAll()
                .stream()
                .map(Recipe::getCategory)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<String> getAllRecipeCuisines() {
        return recipeRepository.findAll()
                .stream()
                .map(Recipe::getCuisine)
                .collect(Collectors.toSet());
    }

    @Override
    public List<RecipeDto> getConvertedRecipes(List<Recipe> recipes) {
        return recipes.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public RecipeDto convertToDto(Recipe recipe) {
        RecipeDto recipeDto = modelMapper.map(recipe, RecipeDto.class);
        UserDto userDto = modelMapper.map(recipe.getUser(), UserDto.class);

        Optional<Image> image = Optional.ofNullable(imageRepository.findByRecipeId(recipe.getId()));
        image.map(img -> modelMapper.map(img, ImageDto.class)).ifPresent(recipeDto::setImageDto);

        List<ReviewDto> reviews = reviewRepository.findAllByRecipeId(recipe.getId())
                .stream()
                .map(review -> modelMapper.map(review,ReviewDto.class)).toList();

        recipeDto.setTotalRateCount(recipeDto.getTotalRateCount());
        recipeDto.setAverageRating(recipeDto.getAverageRating());
        recipeDto.setUser(userDto);
        recipeDto.setReviews(reviews);

        return recipeDto;
    }
}
