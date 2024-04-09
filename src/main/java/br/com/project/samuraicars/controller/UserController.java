package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.DTO.user.UserPublicDetailsRequestBody;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable("user_id") Long userId){
        User user = userService.findById(userId);
        return ResponseEntity.ok(new UserPublicDetailsRequestBody(user.getId(), user.getName(), user.getCreatedAt()));
    }
}
