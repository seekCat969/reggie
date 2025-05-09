package com.seekcat.reggie.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seekcat.reggie.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setUserID(Long id) {
        threadLocal.set(id);
    }

    public static Long getUserID() {
        return threadLocal.get();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        LoginCheckInterceptor.setUserID((Long) request.getSession().getAttribute("employee"));
        LoginCheckInterceptor.setUserID((Long) request.getSession().getAttribute("user"));


        if (request.getSession().getAttribute("employee") != null) {
//            log.warn("用户已登录，ID：{}", request.getSession().getAttribute("employee"));
            return true;
        }
        if (request.getSession().getAttribute("user") != null) {
//            log.warn("用户已登录，ID：{}", request.getSession().getAttribute("employee"));
            return true;
        }
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", Result.error("NOTLOGIN").getMsg());
        map.put("data", Result.error("NOTLOGIN").getData());
        map.put("map", Result.error("NOTLOGIN").getMap());

        String json = objectMapper
                .writerWithDefaultPrettyPrinter()  //开启格式化输出
                .writeValueAsString(map);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        threadLocal.remove();
    }
}
