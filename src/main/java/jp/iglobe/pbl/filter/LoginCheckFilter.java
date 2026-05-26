package jp.iglobe.pbl.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

@Component
public class LoginCheckFilter
extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request,HttpServletResponse response,FilterChain chain)
            throws IOException,
                   ServletException {
    	
        // Session取得
        // ※未ログイン時は作らない
        HttpSession session = request.getSession(false);

        // ログインユーザー
        Object loginUser = null;
        if(session != null) {
            loginUser = session.getAttribute("loginUser");
        }

        // URI取得
        String uri = request.getRequestURI();

        // ログイン不要
        if(uri.equals("/") || uri.equals("/login") 
        		|| uri.endsWith(".css")
                || uri.endsWith(".js")
                || uri.startsWith("/images/")
                || uri.startsWith("/webjars/")) {
            chain.doFilter(request, response);
            return;
        }

        // 未ログイン
        if(loginUser == null) {
            response.sendRedirect("/");
            return;
        }

        // 続行
        chain.doFilter(request, response);
    }
}