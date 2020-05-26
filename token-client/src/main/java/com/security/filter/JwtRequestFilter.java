package com.security.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.service.CustomUserDetailsService;
import com.security.utils.JwtUtil;
import com.security.vo.ErrorDetails;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.RequestContextFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Component
public class JwtRequestFilter extends RequestContextFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain){

            final String authorizationHeader = request.getHeader("Authorization");
            logger.info("authorizationHeader from JwtRequestFilter:{}",authorizationHeader);
            String username = null;
            String jwt = null;
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = jwtUtil.extractUsername(jwt);
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            chain.doFilter(request, response);
        }catch (ServletException ex){
            logger.error("ServletException:{}",ex);
        }catch (IOException ex){
            logger.error("IOException:{}",ex);
        } catch (ExpiredJwtException eje) {
            logger.info("Security exception for user {} - {}", eje.getClaims().getSubject(), eje.getMessage());
            setUnauthorizedResponse(response,eje.getMessage(),jwt);
        }catch (Exception eje) {
            logger.info("Security exception {}",eje.getMessage());
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    public void setUnauthorizedResponse(HttpServletResponse response,String msgDetails,String jwt) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ErrorDetails errorDetails = new ErrorDetails(new Date(),jwt+" Token Expired..",msgDetails);
        try {
            PrintWriter out = response.getWriter();
            out.println(new ObjectMapper().writeValueAsString(errorDetails));
        } catch (IOException e) {
            logger.error("Error", e);
        }
    }

}

