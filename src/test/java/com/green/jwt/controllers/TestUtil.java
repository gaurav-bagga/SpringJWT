package com.green.jwt.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.Charset;
import org.springframework.http.MediaType;

/**
 *
 * @author gaurav.bagga
 */
public class TestUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
    
    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
    
    
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return OBJECT_MAPPER.writeValueAsBytes(object);
    }
    
}
