package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.DTO.user.UserPostRequestBody;
import br.com.project.samuraicars.DTO.user.UserResponseBody;
import br.com.project.samuraicars.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping
    public ResponseEntity<UserResponseBody> save(@Valid @RequestBody UserPostRequestBody user) {
        return ResponseEntity.ok().body(userService.save(user));
    }
}
