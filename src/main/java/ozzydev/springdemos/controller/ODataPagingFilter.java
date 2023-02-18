//package ozzydev.springdemos.controller;
//
//import org.springframework.core.annotation.Order;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//@WebFilter("/odata/*")
//@Order(1)
//public class ODataPagingFilter implements Filter
//{
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException
//    {
//        System.out.println("CustomFilter:init()");
//    }
//
//    @Override
//    public void destroy()
//    {
//        System.out.println("CustomFilter:destroy()");
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
//    {
//
//        System.out.println("request full path->" + ((HttpServletRequest) servletRequest).getRequestURL() +
//                "?" + ((HttpServletRequest) servletRequest).getQueryString());
//        System.out.println("query request URI->" + ((HttpServletRequest) servletRequest).getRequestURI());
//        System.out.println("query string->" + ((HttpServletRequest) servletRequest).getQueryString());
//        System.out.println("request URL->" + ((HttpServletRequest) servletRequest).getRequestURL());
//        System.out.println("path info->" + ((HttpServletRequest) servletRequest).getPathInfo());
//        System.out.println("context path info->" + ((HttpServletRequest) servletRequest).getContextPath());
//        System.out.println("servlet path info->" + ((HttpServletRequest) servletRequest).getServletPath());
//
//
//        MutableHttpRequest mutableHttpRequest = new MutableHttpRequest((HttpServletRequest) servletRequest);
//
//        if (
//                !servletRequest.getParameterMap().containsKey("$top")
//               // && !((HttpServletRequest) servletRequest).getPathInfo().contains("$metadata")
//        )
//        {
//
//            System.out.println("pagination filter parameter $top not found.");
//            throw new ServletException("pagination filter parameter $top not found.");
//        }
//
//        var httpResponse = (HttpServletResponse) servletResponse;
//        //httpResponse.addHeader("Accept", "application/json;odata.metadata=minimal");
//        httpResponse.addHeader("Accept", "application/json;odata.metadata=full");
//        filterChain.doFilter(mutableHttpRequest, httpResponse);
//
//        //filterChain.doFilter(mutableHttpRequest, servletResponse);
//
//        System.out.println("doFilter called");
//        mutableHttpRequest.getParameterMap().forEach((key, value) -> {
//            System.out.println("param key->" + key + "  param value->" + value[0]);
//        });
//
////        if (mutableHttpRequest.getParameterMap().containsKey("$top"))
////        {
////            System.out.println("paging filter exists");
////
////            filterChain.doFilter(mutableHttpRequest, servletResponse);
////
////        }
////        else
////        {
////            System.out.println("paging filter NOT FOUND");
////            //mutableHttpRequest.getParameterMap().put("$top", new String[]{"5"});
////            mutableHttpRequest.addParameter("$top", "5");
////
////            Map<String, String[]> additionalParams = new HashMap<String, String[]>();
////            additionalParams.put("$top", new String[]{"5"});
////
////            MutableHttpRequest2 mutableHttpRequest2 = new MutableHttpRequest2((HttpServletRequest) servletRequest, additionalParams);
////            filterChain.doFilter(mutableHttpRequest, servletResponse);
////            //servletRequest.getRequestDispatcher("url").forward(mutableHttpRequest, servletResponse);
////
////
////        }
//
//    }
//
//}
//
//
