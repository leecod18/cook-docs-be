package com.andrewbycode.cookdocs.utils;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;

import java.util.Base64;

public class FileUtils {
    public static MediaType getMediaType(String fileName){
        String extension = getFileExtension(fileName);
        switch (extension.toLowerCase()){
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "gif":
                return MediaType.IMAGE_GIF;
            case "pdf":
                return MediaType.APPLICATION_PDF;
            case "json":
                return MediaType.APPLICATION_JSON;
            case "xml":
                return MediaType.APPLICATION_XML;
            case "html":
                return MediaType.TEXT_HTML;
            case "txt":
                return MediaType.TEXT_PLAIN;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    public static String toBase64(@NonNull byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static boolean validImageFile(String fileName, String[]  validExtensions){
        if(fileName == null || fileName.isEmpty()){
            return false;
        }
        String fileExtension = getFileExtension(fileName);
        for(String extension : validExtensions){
            if(fileExtension.equalsIgnoreCase(extension)){
                return true;
            }
        }
        return false;
    }

    public static boolean validImageSize(String codeBase64 , int fileSize){
        long base64Length = codeBase64.length();

        // 3 byte gốc -> 4 ký tự base 64
        long estimatedSizeInBytes = (base64Length * 3) / 4;
        return estimatedSizeInBytes <= fileSize;
    }

    public static String getFileExtension(String fileName){
        int dotIndex = fileName.lastIndexOf(".");
        if(dotIndex == -1){
            return "";
        }
        return fileName.substring(dotIndex + 1);
    }




}
