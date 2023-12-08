package com.demoagro.loginservice.controllers;

import com.demoagro.loginservice.model.request.LoginRequest;
import com.demoagro.loginservice.model.request.RefreshTokenRequest;
import com.demoagro.loginservice.constants.LoginServiceConstants;
import com.demoagro.loginservice.model.response.ApplicationResponse;
import com.demoagro.loginservice.model.response.LoginResponse;
import com.demoagro.loginservice.model.response.RefreshTokenResponse;
import com.demoagro.loginservice.service.impl.RefreshTokenService;
import com.demoagro.loginservice.service.interfaces.LoginService;
import com.demoagro.loginservice.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(LoginServiceConstants.AUTH)
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    @Autowired
    RefreshTokenService refreshTokenService;

    /**
     * @param loginRequest
     * @return
     */
    @PostMapping(LoginServiceConstants.TOKEN)
    public ResponseEntity<ApplicationResponse<LoginResponse>> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {

        final LoginResponse loginResponse = loginService.loadUser(loginRequest);

        return new ResponseEntity<ApplicationResponse<LoginResponse>>(
                new ApplicationResponse<LoginResponse>(loginResponse, null), HttpStatus.OK);
    }

    /**
     * @param refreshTokenRequest
     * @return
     */
    @PostMapping(LoginServiceConstants.REFRESH_TOKEN)
    public ResponseEntity<ApplicationResponse<RefreshTokenResponse>> getRefreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {

        final RefreshTokenResponse refreshTokenResponse = refreshTokenService.getRefreshToken(refreshTokenRequest);

        return new ResponseEntity<ApplicationResponse<RefreshTokenResponse>>(
                new ApplicationResponse<RefreshTokenResponse>(refreshTokenResponse, null), HttpStatus.OK);
    }

    @GetMapping(LoginServiceConstants.EMAIL_AVAILABLE)
    public ResponseEntity<ApplicationResponse<Boolean>> isEmailAvailable(
            @RequestParam(required = true, name = LoginServiceConstants.EMAIL) String email) {

        boolean isAvailable = userService.isEmailAvailable(email);
        return new ResponseEntity<ApplicationResponse<Boolean>>(new ApplicationResponse<Boolean>(isAvailable, null),
                HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping(LoginServiceConstants.AUTH_LOGOUT)
    public ResponseEntity<ApplicationResponse<Boolean>> logout(@PathVariable(required = true) Long userId) {
        loginService.deleteUserSession(userId);
        return new ResponseEntity<ApplicationResponse<Boolean>>(new ApplicationResponse<Boolean>(true, null),
                HttpStatus.OK);
    }

}
