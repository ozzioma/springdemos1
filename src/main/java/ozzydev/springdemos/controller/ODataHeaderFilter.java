//package ozzydev.springdemos.controller;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebFilter("/filter-response-header/*")
//public class ODataHeaderFilter implements Filter
//{
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException
//    {
//        Filter.super.init(filterConfig);
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
//    {
//        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
//        httpServletResponse.setHeader(
//                "Accept", "application/json;odata.metadata=minimal");
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    @Override
//    public void destroy()
//    {
//        Filter.super.destroy();
//    }
//}
