package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.DTO.authentication.TokenJWTResponseBody;
import br.com.project.samuraicars.DTO.authentication.UserAuthorizationRequestBody;
import br.com.project.samuraicars.DTO.authentication.UserRegisterPostRequestBody;
import br.com.project.samuraicars.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authorization")
public class AuthorizationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    @Operation(description = "Register a new user")
    public ResponseEntity<Void> register(@Valid @RequestBody UserRegisterPostRequestBody user) {
        authenticationService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/authorize")
    @Operation(description = "Authorize user and generate JWT Token")
    public ResponseEntity<TokenJWTResponseBody> authorize(@Valid @RequestBody UserAuthorizationRequestBody user) {
        return ResponseEntity.ok().body(new TokenJWTResponseBody(authenticationService.authorize(user)));
    }
}
