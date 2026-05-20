package com.example.bibliotekbackenden;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@Component
public class ResponseTimeFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(ResponseTimeFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        chain.doFilter(request, response);
        long duration = System.currentTimeMillis() - startTime;
        HttpServletRequest req = (HttpServletRequest) request;

        logger.info("Request URL: {}, Duration: {} ms", req.getRequestURL(), duration);
    }
}