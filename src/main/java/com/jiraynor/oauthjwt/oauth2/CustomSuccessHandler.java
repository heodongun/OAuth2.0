package com.jiraynor.oauthjwt.oauth2;

import com.jiraynor.oauthjwt.dto.CustomOAuth2User;
import com.jiraynor.oauthjwt.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("request = " + request);
        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        System.out.println("username = " + username);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, 60*60*60L);
        System.out.println("token = " + token);
//        response.addCookie(UncreateCookie("access", token));
//        response.addCookie(createCookie("refresh", token));
//        response.addCookie(createCookie("Authorization", token));
        // Spring에서 ResponseCookie를 이용해서 쿠키를 생성할 수 있다.
        ResponseCookie strictCookie = ResponseCookie.from("strict", token)
                .path("/")
                .sameSite("strict")
                .domain("localhost")
                .build();
        response.addHeader("Set-Cookie", strictCookie.toString());

        ResponseCookie laxCookie = ResponseCookie.from("Lax", token)
                .path("/")
                .sameSite("Lax")
                .domain("localhost")
                .build();
        response.addHeader("Set-Cookie", laxCookie.toString());

        ResponseCookie noneCookie = ResponseCookie.from("none", token)
                .path("/")
                .sameSite("none")
                .domain("localhost")
                .build();
        response.addHeader("Set-Cookie", noneCookie.toString());
        response.addHeader("hello","heodongun");
        response.sendRedirect("http://localhost:3000/oauth-success?accessToken="+token);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
    private Cookie UncreateCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setPath("/");
        return cookie;
    }
}
