package com.andrewbycode.cookdocs.controller;

import com.andrewbycode.cookdocs.dto.ImageDto;
import com.andrewbycode.cookdocs.entitys.Image;
import com.andrewbycode.cookdocs.request.ImageRequest;
import com.andrewbycode.cookdocs.service.image.ImageService;
import com.andrewbycode.cookdocs.storage.StorageService;
import com.andrewbycode.cookdocs.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;
    private final StorageService storageService;
    @Value("${minio.bucket}")
    private String BUCKET_NAME;

    @PostMapping("/upload")
    public ResponseEntity<ImageDto> uploadImage(@RequestBody ImageRequest imageRequest) throws BadRequestException {
        ImageDto imageDto = imageService.saveOrUpdateImage(imageRequest);
        return ResponseEntity.ok(imageDto);
    }

    @DeleteMapping("/image/{imageId}/delete")
    public ResponseEntity<String> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImageById(imageId);
        return ResponseEntity.ok("Delete Image success");
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) {
        Image image = imageService.getImageById(imageId);
        byte[] imageBytes = this.storageService.getObjectFromMinio(BUCKET_NAME, image.getFilePath(), true);
        if (imageBytes.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        ByteArrayResource resource = new ByteArrayResource(imageBytes);
        String fileType = FileUtils.getFileExtension(image.getFileName());
        return ResponseEntity.ok()
                .contentType(FileUtils.getMediaType(fileType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName() + "\"")
                .body(resource);
    }


}
