package com.andrewbycode.cookdocs.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {
    public static byte[] toByteArray(InputStream input) throws IOException{
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input,output);
        return output.toByteArray();
    }

    public static long copy(InputStream input, OutputStream output) throws IOException{
        return copy(input, output, 8024);
    }

    public static long copy(InputStream input, OutputStream output,int buffersize) throws  IOException {
        if(buffersize < 1){
           throw new IllegalArgumentException("buffer size must be bigger than 0");
       } else {
             byte[] buffer = new byte[buffersize];
             int n = 0;
             long count;

             for(count = 0L; -1 != (n = input.read(buffer));count += (long) n){
                 output.write(buffer,0,n);
            }
            return count;
        }
    }

}
