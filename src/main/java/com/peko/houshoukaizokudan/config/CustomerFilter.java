package com.peko.houshoukaizokudan.config;

import com.peko.houshoukaizokudan.model.Member;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class CustomerFilter  implements Filter {



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        boolean isAuthorized = false;

        if (session == null) {
            isAuthorized = httpRequest.getRequestURI().startsWith("/public");
            handleAuthorization(isAuthorized, request, response, chain);
            return;
        }

        Member loginUser = (Member) session.getAttribute("loginUser");
        if (loginUser == null) {
            isAuthorized = httpRequest.getRequestURI().startsWith("/public/") || httpRequest.getRequestURI().equals("/public");
            handleAuthorization(isAuthorized, request, response, chain);
            return;
        }


        Integer membertyprid = loginUser.getMembertypeid().getId();
        if (membertyprid < 3) {
            isAuthorized = httpRequest.getRequestURI().startsWith("/public") ||
                    httpRequest.getRequestURI().startsWith("/seller") ||
                    httpRequest.getRequestURI().startsWith("/customer");
            handleAuthorization(isAuthorized, request, response, chain);
            return;
        }

        if (membertyprid == 3) {
            isAuthorized = httpRequest.getRequestURI().startsWith("/public") ||
                    httpRequest.getRequestURI().startsWith("/customer");
            handleAuthorization(isAuthorized, request, response, chain);
            return;
        }

        handleAuthorization(isAuthorized, request, response, chain);
    }

    private void handleAuthorization(boolean isAuthorized, ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
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
