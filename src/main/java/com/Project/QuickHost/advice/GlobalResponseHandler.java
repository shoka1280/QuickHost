package com.Project.QuickHost.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;//apply to all responses
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        List<String> allowedRoutes=List.of("/v3/api-docs","/actuator");
        boolean isAllowed=allowedRoutes
                .stream()
                .anyMatch(route -> request.getURI().getPath().contains(route));
        if(body instanceof ApiError || isAllowed) {
            return body; // Return the body as is if it's an ApiError or allowed route
        }
        return new ApiResponse<>(body);
    }
}
