package com.techplanner.componentservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final Set<String> METHODS_WITH_BODY = Set.of("POST", "PUT", "PATCH", "DELETE");
    private static final int MAX_BODY_LENGTH = 1200;

    private final ObjectMapper objectMapper;

    public RequestLoggingFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request, MAX_BODY_LENGTH);
        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("HTTP {} {} -> {} ({} ms)",
                    wrappedRequest.getMethod(),
                    requestPath(wrappedRequest),
                    response.getStatus(),
                    duration);

            if (METHODS_WITH_BODY.contains(wrappedRequest.getMethod())) {
                String body = requestBody(wrappedRequest);
                if (!body.isBlank()) {
                    log.info("Body enviado:\n{}", body);
                }
            }
        }
    }

    private String requestPath(HttpServletRequest request) {
        String query = request.getQueryString();
        if (query == null || query.isBlank()) {
            return request.getRequestURI();
        }
        return request.getRequestURI() + "?" + query;
    }

    private String requestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length == 0) {
            return "";
        }

        String rawBody = new String(content, StandardCharsets.UTF_8).trim();
        if (rawBody.length() > MAX_BODY_LENGTH) {
            rawBody = rawBody.substring(0, MAX_BODY_LENGTH) + "...";
        }

        try {
            return objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(objectMapper.readTree(rawBody));
        } catch (IOException ex) {
            return rawBody;
        }
    }
}
