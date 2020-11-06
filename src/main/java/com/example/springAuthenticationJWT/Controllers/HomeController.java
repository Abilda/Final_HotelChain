package com.example.springAuthenticationJWT.Controllers;

import com.example.springAuthenticationJWT.security.jwt.JwtUtils;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

@RestController
@CrossOrigin
@RequestMapping("/api/home")
public class HomeController {
    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/index")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping("/CheckJwtValidity")
    public Boolean checkValidity(@PathParam("authToken") String authToken) {
        System.out.println(authToken);
        return jwtUtils.validateJwtToken(authToken);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("I was here");
        //redirect
    }
}
