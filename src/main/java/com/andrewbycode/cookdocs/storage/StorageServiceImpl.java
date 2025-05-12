package com.andrewbycode.cookdocs.storage;


import com.andrewbycode.cookdocs.exception.InternalServerErrorException;
import com.andrewbycode.cookdocs.utils.FileUtils;
import com.andrewbycode.cookdocs.utils.IOUtils;
import com.andrewbycode.cookdocs.utils.StringUtils;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final MinioClient minioClient;

    @Override
    public boolean putObjectToMinio(String bucketName, String fileName, byte[] bytes, boolean isRequire) {
        if (StringUtils.isNullOrBlank(fileName) || StringUtils.isNullOrBlank(bucketName) || bytes.length == 0) {
            return false;
        }

        try (ByteArrayInputStream bai = new ByteArrayInputStream(bytes)) {
            return minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .contentType(FileUtils.getMediaType(fileName).toString())
                    .stream(bai, bytes.length, -1)
                    .build()) != null;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (isRequire) {
                throw new InternalServerErrorException("Upload file from minio failed.");
            }
        }
        return false;
    }

    @Override
    public byte[] getObjectFromMinio(String bucketName, String fileName, boolean isRequire) {
        if (fileName != null && this.isExistedBucket(bucketName)) {
            try (InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build())) {
                return IOUtils.toByteArray(inputStream);
            } catch (Exception e) {
                log.error(e.getMessage());
                if (isRequire) {
                    throw new InternalServerErrorException("Download file from minio failed.");
                }
            }
        }
        return new byte[0];
    }

    @Override
    public void removeObjectFromMinio(String bucketName, String fileName, boolean isRequire) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new InternalServerErrorException("Remove file from minio failed");
        }

    }

    private boolean isExistedBucket(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error system");
        }
    }


}
