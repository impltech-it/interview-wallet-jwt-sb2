package com.interview.task.service;

import com.interview.task.dto.JwtAuthenticationResponse;
import com.interview.task.dto.LoginRequest;
import com.interview.task.dto.SignUpRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * Interface represents authentication service methods.
 */
public interface AuthenticationService {
    JwtAuthenticationResponse signIn(LoginRequest loginRequest, HttpServletResponse response);
    JwtAuthenticationResponse signUp(SignUpRequest signUpRequest, HttpServletResponse response);
}
