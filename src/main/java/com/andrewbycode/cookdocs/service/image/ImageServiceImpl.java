package com.andrewbycode.cookdocs.service.image;

import com.andrewbycode.cookdocs.dto.ImageDto;
import com.andrewbycode.cookdocs.entitys.Image;
import com.andrewbycode.cookdocs.entitys.Recipe;
import com.andrewbycode.cookdocs.enums.ImageTypeEnum;
import com.andrewbycode.cookdocs.exception.BadRequestException;
import com.andrewbycode.cookdocs.repository.ImageRepository;
import com.andrewbycode.cookdocs.request.ImageRequest;
import com.andrewbycode.cookdocs.service.recipe.RecipeService;
import com.andrewbycode.cookdocs.service.user.UserService;
import com.andrewbycode.cookdocs.storage.StorageService;
import com.andrewbycode.cookdocs.utils.FileUtils;
import com.andrewbycode.cookdocs.utils.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final RecipeService recipeService;
    private final StorageService storageService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Value("${minio.bucket}")
    private String BUCKET_NAME;

    private final String[] FILE_TYPE = {"jpeg","jpg","png"};
private final String SIZE_IMAGE = "1MB";
    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Image not found"));
    }

    @Override
    @Transactional
    public void deleteImageById(Long imageId) {
        Image image = getImageById(imageId);
        Recipe recipe = image.getRecipe();
        recipe.setImage(null);
        imageRepository.deleteById(imageId);
    }

    @Override
    @Transactional
    public ImageDto saveOrUpdateImage(ImageRequest imageRequest) throws BadRequestException {

        validFileImage(imageRequest.getFileName(), imageRequest.getFileContent());

        Long ownerId = imageRequest.getUserId() != null ? imageRequest.getUserId() : imageRequest.getRecipeId();
        int valueType = imageRequest.getImageType();

        // Tìm ảnh cũ
        Image oldImage = imageRepository.findByUserIdAndRecipeId(imageRequest.getUserId(), imageRequest.getRecipeId());

            if (oldImage != null) {
                storageService.removeObjectFromMinio(BUCKET_NAME, oldImage.getFilePath(), false);
                if (oldImage.getRecipe() != null) {
                    oldImage.getRecipe().setImage(null);
                } else {
                    oldImage.getUser().setImage(null);
                }
                imageRepository.delete(oldImage);
                imageRepository.flush();
            }

        // Lưu ảnh mới
        String filePath = createFilePath(ownerId, imageRequest.getFileName(), valueType);
        Image image = new Image();
        image.setFileName(imageRequest.getFileName());
        image.setFilePath(filePath);
        image.setImageType(valueType);

        switch (valueType){
            case 1:
                image.setRecipe(null);
                image.setUser(userService.getUserById(imageRequest.getUserId()));
                break;
            case 2:
                image.setUser(null);
                image.setRecipe(recipeService.getRecipeById(imageRequest.getRecipeId()));
                break;
            default:
                throw new IllegalArgumentException("Invalid value type: " + valueType);
        }

        Image saveImage = imageRepository.save(image);

        // Upload file
        byte[] fileBytes = Base64.getDecoder().decode(imageRequest.getFileContent());
        storageService.putObjectToMinio(BUCKET_NAME, filePath, fileBytes, true);

        return modelMapper.map(saveImage,ImageDto.class);
    }

    private String createFilePath(Long id, String fileName, int value) {
        String filePath = "ImageProfile/"
                + StringUtils.dateToString(new Date(), StringUtils.DD_MM_YYYY_2)
                + StringUtils.FORWARD_SLASH
                + id
                + StringUtils.FORWARD_SLASH
                + fileName;
        if (ImageTypeEnum.RECIPE.getValue() == value) {
            filePath = "ImageRecipe/"
                    + StringUtils.dateToString(new Date(), StringUtils.DD_MM_YYYY_2)
                    + StringUtils.FORWARD_SLASH
                    + id
                    + StringUtils.FORWARD_SLASH
                    + fileName;
        }
        return filePath;
    }

    private void validFileImage(String fileName, String fileContent) throws BadRequestException {
        if(!FileUtils.validImageFile(fileName,FILE_TYPE)){
            throw new BadRequestException(String.format("The image file is in an incorrect format [%s]", Arrays.toString(FILE_TYPE)));
        }

        if(!FileUtils.validImageSize(fileContent,1 * 1024 * 1024)){
            throw new BadRequestException(String.format("The image file size must be smaller [%s]",SIZE_IMAGE));
        }


    }

}
