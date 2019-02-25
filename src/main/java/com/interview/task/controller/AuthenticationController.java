package com.interview.task.controller;

import com.interview.task.dto.LoginRequest;
import com.interview.task.dto.SignUpRequest;
import com.interview.task.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Authentication controller.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authService;

    @Autowired
    public AuthenticationController(final AuthenticationService authService) {
        this.authService = authService;
    }

    /**
     * Method performs login operation for existing user.
     *
     * @param loginRequest valid user login and password.
     * @param response HttpServletResponse
     * @return ResponseEntity
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Valid final LoginRequest loginRequest,
                                    final HttpServletResponse response) {
        return ResponseEntity.ok().body(authService.signIn(loginRequest, response));
    }

    /**
     * Method performs registration operation for new users.
     *
     * @param signUpRequest contains user information.
     * @param response HttpServletResponse
     * @return ResponseEntity
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid final SignUpRequest signUpRequest,
                                    final HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(signUpRequest, response));
    }
}
