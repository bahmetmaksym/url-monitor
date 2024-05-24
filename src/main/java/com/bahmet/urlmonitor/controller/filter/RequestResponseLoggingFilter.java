package com.bahmet.urlmonitor.controller.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
@Slf4j
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            logRequest(wrappedRequest);
            logResponse(wrappedResponse);
            wrappedResponse.copyBodyToResponse();  // Ensure response body is written to the client
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String requestBody = new String(request.getContentAsByteArray()).replaceAll("\\s+", "").trim();
        log.info("Request: \nMethod: {}\nURI: {}\nQuery: {}\nBody: {}", request.getMethod(), request.getRequestURI(), request.getQueryString(), requestBody);
    }

    private void logResponse(ContentCachingResponseWrapper response) {
        String responseBody = new String(response.getContentAsByteArray());
        log.info("Response Body: {}", responseBody);
    }
}
