package com.andrewbycode.cookdocs.storage;

public interface StorageService {
    boolean putObjectToMinio(String bucketName, String filePath, byte[] bytes, boolean isRequire);
    byte[] getObjectFromMinio(String bucketName, String filePath,  boolean isRequire);
    void removeObjectFromMinio(String bucketName, String filePath,  boolean isRequire);

}
