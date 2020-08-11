package com.xuliang.cim.server.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;


@Slf4j
public class JsonV2Util {

    public static final ObjectMapper objectMapper = createObjectMapperUsingLowerCamelCase();



    public static ObjectMapper createObjectMapperUsingLowerCamelCase() {
        return createObjectMapper(PropertyNamingStrategy.LOWER_CAMEL_CASE);
    }


    static ObjectMapper createObjectMapper(PropertyNamingStrategy strategy) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(strategy);
        mapper.setSerializationInclusion(Include.ALWAYS);
        // disabled features:
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }




    public static String writeValue(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(String str, Class<T> clazz) {
        try {
            return objectMapper.readValue(str, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Map<String, Object> toMap(Object object) {
        return objectMapper.convertValue(object, Map.class);
    }

    public static Map<String, String> toMapStr(Object object) {
        return objectMapper.convertValue(object, Map.class);
    }

}
