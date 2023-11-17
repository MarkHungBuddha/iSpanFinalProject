package com.peko.houshoukaizokudan.config;

import com.peko.houshoukaizokudan.model.Member;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class CustomerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);

        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        boolean isAuthorized = false;
        String requestURI = httpRequest.getRequestURI();

        if (session == null) {
            isAuthorized = requestURI.startsWith("/public");
            handleAuthorization(isAuthorized, false, request, response, chain);
            return;
        }

        Member loginUser = (Member) session.getAttribute("loginUser");

        if (loginUser == null) {
            isAuthorized = requestURI.startsWith("/public");
            handleAuthorization(isAuthorized, false, request, response, chain);
            return;
        }

        Integer membertypeid = loginUser.getMembertypeid().getId();
        if (membertypeid == 3) {
            isAuthorized = requestURI.startsWith("/public") || requestURI.startsWith("/customer");
        } else if (membertypeid < 3) {
            isAuthorized = requestURI.startsWith("/public") || requestURI.startsWith("/customer") || requestURI.startsWith("/seller");
        }

        handleAuthorization(isAuthorized, true, request, response, chain);
    }

    private void handleAuthorization(boolean isAuthorized, boolean isUserLoggedIn, ServletRequest request,
                                     ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isAuthorized) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (!isUserLoggedIn) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 401 error for not logged in
        } else {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN); // 403 error for logged in but unauthorized
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
