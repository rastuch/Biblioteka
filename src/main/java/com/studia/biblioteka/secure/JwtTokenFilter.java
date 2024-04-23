package com.studia.biblioteka.secure;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtTokenFilter extends OncePerRequestFilter {
    private TokenStoreService tokenStoreService;


    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws ServletException, IOException {
        System.out.println("startuje");

        if (tokenStoreService == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            tokenStoreService = context.getBean(TokenStoreService.class);
        }


        try {
            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!tokenStoreService.isTokenValid(token.replace("Bearer ", ""))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                System.out.println("token expired");
                return; // Token nie jest ważny
            }

            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token.replace("Bearer ", ""));
            Long userId = jwt.getClaim("userId").asLong();
            String role = jwt.getClaim("role").asString();
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userId, null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (JWTVerificationException exception) {
            // Obsługa wyjątków związanych z JWT
            System.out.println(exception.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
