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
            handleAuthorization(isAuthorized, request, response, chain);
            return;
        }

        Member loginUser = (Member) session.getAttribute("loginUser");

        if (loginUser == null) {
            isAuthorized = requestURI.startsWith("/public");
            handleAuthorization(isAuthorized, request, response, chain);
            return;
        }

        Integer membertypeid = loginUser.getMembertypeid().getId();
        if (membertypeid == 3) {
            isAuthorized = requestURI.startsWith("/public") || requestURI.startsWith("/customer");
        } else if (membertypeid < 3) {
            isAuthorized = requestURI.startsWith("/public") || requestURI.startsWith("/customer") || requestURI.startsWith("/seller");
        }

        handleAuthorization(isAuthorized, request, response, chain);
    }


    private void handleAuthorization(boolean isAuthorized, ServletRequest request,
                                     ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isAuthorized) {
            chain.doFilter(request, response);
            return;
        }

        ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}