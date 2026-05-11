package com.Project.QuickHost.Security.Filter;

import com.Project.QuickHost.Entity.User;
import com.Project.QuickHost.Security.JWTservice;
import com.Project.QuickHost.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExceptionResolver;


import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class JwtAuthFilter  extends OncePerRequestFilter {
    private final JWTservice jwtService;
    private final UserService userService;
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try
        {
            final String requestTokenHeader=request.getHeader("Authorization");//header we are intrested  is Auhtorization ,head are nothing but key value pair header fo http:Authorization: Bearer <JWT_token>
            //every token has pre fix bearer xwdnoefnoie....."
            if(requestTokenHeader==null||!requestTokenHeader.startsWith("Bearer"))
            {
                filterChain.doFilter(request,response);
                return;
            }
            //every token has pre fix "Bearer xwdnoefnoie....."

            String tokenHeader=requestTokenHeader.substring(7);
            Long id =jwtService.getUserIdFromToken(tokenHeader);
            //if id is valid,but no authentication is present in seurity context
            if(id>0 && SecurityContextHolder.getContext().getAuthentication()==null)
            {
                //we need to authenticate the user
                //load user by id
                User user=userService.getUserById(id);
                UsernamePasswordAuthenticationToken userPassAuth=
                        new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                //aading details to user
                userPassAuth.setDetails
                        (new WebAuthenticationDetailsSource().buildDetails(request)//contains the info regarding the request such as IP address, session ID, etc.
                        );
                //Adding the user to securty context
                SecurityContextHolder.getContext().setAuthentication(userPassAuth);
            }
            filterChain.doFilter(request,response);
        }
        catch (Exception ex){
            handlerExceptionResolver.resolveException(request,response,null,ex);
        }//now filter chain will get resolved by the global exception handler




    }
}
