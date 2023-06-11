package my.backend.library.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JsonUtils {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static String getJson(Object object) {
        try {
            return (new ObjectMapper()).writeValueAsString(object);
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return (new ObjectMapper()).readValue(json, typeReference);
        } catch (IOException var3) {
            throw new RuntimeException("Can't parse json" + var3.getMessage());
        }
    }
}
