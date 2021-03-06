///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package util;
//
//import java.io.IOException;
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
///**
// *
// * @author ushiho
// */
//@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.xhtml"})
//public class AuthFilterUtil implements Filter {
//
//    public AuthFilterUtil() {
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        try {
//
//            // check whether session variable is set
//            HttpServletRequest req = (HttpServletRequest) request;
//            HttpServletResponse res = (HttpServletResponse) response;
//            HttpSession ses = req.getSession(false);
//            //  allow user to proceed if url is login.xhtml or user logged in or user is accessing any page in //public folder
//            String reqURI = req.getRequestURI();
//            if (reqURI.contains("/login.xhtml") || (ses != null && ses.getAttribute("user") != null)
//                    || reqURI.contains("/faces/index.xhtml") || reqURI.contains("javax.faces.resource")) {
//                chain.doFilter(request, response);
//            } else // user didn't log in but asking for a page that is not allowed so take user to login page
//            {
//                res.sendRedirect(req.getContextPath() + "/faces/template/index/login.xhtml");  // Anonymous user. Redirect to login page
//            }
//        } catch (IOException | ServletException t) {
//            System.out.println(t.getMessage());
//        }
//    } //doFilter
//
//    @Override
//    public void destroy() {
//
//    }
//}
