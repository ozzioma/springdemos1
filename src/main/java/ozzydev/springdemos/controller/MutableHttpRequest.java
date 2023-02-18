//package ozzydev.springdemos.controller;
//
//import java.util.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
//
//public class MutableHttpRequest extends HttpServletRequestWrapper
//{
//
//    private final Map<String, String[]> mutableParams = new HashMap<>();
//
//    public MutableHttpRequest(final HttpServletRequest request)
//    {
//        super(request);
//    }
//
//    public MutableHttpRequest addParameter(String name, String value)
//    {
//        if (value != null)
//            mutableParams.put(name, new String[]{value});
//
//        return this;
//    }
//
//    @Override
//    public String getParameter(final String name)
//    {
//        if (name.equals("$top"))
//        {
//            System.out.println("$top filter requested");
//        }
//
//        var value = getParameterMap().get(name);
//        if (value == null)
//        {
//            return null;
//        }
//        else
//        {
//            return getParameterMap().get(name)[0];
//        }
//
////        String[] values = getParameterMap().get(name);
////
////        return Arrays.stream(values)
////                .findFirst()
////                .orElse(super.getParameter(name));
//
//
////        if (name.equals("$top"))
////        {
////            System.out.println("$top filter requested");
////
////            var top = super.getParameter(name);
////            if (top == null)
////            {
////                return "5";
////            }
////            return top;
////        }
////        else
////        {
////            return super.getParameter(name);
////        }
//
//    }
//
//    @Override
//    public Map<String, String[]> getParameterMap()
//    {
//        Map<String, String[]> allParameters = new HashMap<>();
//        allParameters.putAll(super.getParameterMap());
//
//        if (!super.getParameterMap().containsKey("$top"))
//        {
//            System.out.println("paging filter NOT FOUND");
//            addParameter("$top", "5");
//            allParameters.putAll(mutableParams);
//        }
//
//
//        System.out.println("mutable getParamMap called");
//        allParameters.forEach((key, value) -> {
//            System.out.println("param key->" + key + "  param value->" + value[0]);
//        });
//
//        return Collections.unmodifiableMap(allParameters);
//    }
//
//    @Override
//    public Enumeration<String> getParameterNames()
//    {
//        return Collections.enumeration(getParameterMap().keySet());
//    }
//
//    @Override
//    public String[] getParameterValues(final String name)
//    {
//        return getParameterMap().get(name);
//    }
//}
//
//
//
//
//
