//package jp.iglobe.pbl.filter;
//
//import java.io.IOException;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class LoginCheckFilter
//extends HttpFilter {
//
// @Override
//    protected void doFilter(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpSession session = request.getSession();
//
//        // ログインユーザー
//        Object loginUser = session.getAttribute("loginUser");
//
//        // URL取得
//        String uri = request.getRequestURI();
//
//        // ログイン不要URL
//        if(uri.equals("/") || uri.equals("/login") || uri.contains("css") || uri.contains("js") || uri.contains("images")) {
//
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // 未ログイン
//        if(loginUser == null) {
//        	response.sendRedirect("/");
//            return;
//        }
//
//        // 続行
//        chain.doFilter(request, response);
//    }
//}