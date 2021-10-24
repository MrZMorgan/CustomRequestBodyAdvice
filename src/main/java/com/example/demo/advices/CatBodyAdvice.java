package com.example.demo.advices;

import com.example.demo.pojos.Cat;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.stream.Stream;

@RestControllerAdvice
public class CatBodyAdvice implements RequestBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        final var methodAnnotations = methodParameter.getMethodAnnotations();

        final var isRequestMappingPresent =
            Stream.of(methodAnnotations).anyMatch(annotation -> annotation instanceof RequestMapping);

        final var requestMethod =
            Arrays.stream(
                ((RequestMapping) Arrays.stream(methodAnnotations)
                    .filter(annotation -> annotation instanceof RequestMapping)
                    .findFirst()
                    .get()
                ).method()
            ).findFirst().get();

        return requestMethod == RequestMethod.PUT || requestMethod == RequestMethod.POST;


//        return Arrays.stream(methodParameter.getMethodAnnotations())
//            .anyMatch(a -> a.annotationType().equals(PostMapping.class));
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @SneakyThrows
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (body instanceof Cat) {
            Field[] fields = body.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().equals("name")) {
                    field.setAccessible(true);
                    field.set(body, field.get(body) + "!!!");
                }
            }
        }

        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
