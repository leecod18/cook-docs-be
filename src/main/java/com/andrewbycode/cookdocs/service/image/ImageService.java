package com.andrewbycode.cookdocs.service.image;

import com.andrewbycode.cookdocs.dto.ImageDto;
import com.andrewbycode.cookdocs.entitys.Image;
import com.andrewbycode.cookdocs.request.ImageRequest;
import org.apache.coyote.BadRequestException;

public interface ImageService {
    Image getImageById(Long imageId);
    void deleteImageById(Long imageId);
    ImageDto saveOrUpdateImage(ImageRequest imageRequest) throws BadRequestException;
}
