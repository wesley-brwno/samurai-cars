package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.DTO.user.UserPostRequestBody;
import br.com.project.samuraicars.DTO.user.UserResponseBody;
import br.com.project.samuraicars.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping
    public ResponseEntity<UserResponseBody> save(@Valid @RequestBody UserPostRequestBody user, UriComponentsBuilder uriBuilder) {
        UserResponseBody savedUser = userService.save(user);
        URI uri = uriBuilder.path("/users/{id}").buildAndExpand(savedUser.id()).toUri();
        return ResponseEntity.created(uri).body(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseBody> list(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.list(id));
    }
}
