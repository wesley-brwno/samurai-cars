package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.DTO.user.UserDetailsRequestBody;
import br.com.project.samuraicars.DTO.user.UserPublicDetailsRequestBody;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping()
    public ResponseEntity<UserDetailsRequestBody> getUserByAuthorizationHeader(
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(new UserDetailsRequestBody(user.getId(), user.getName(), user.getAuthorities().toString()));
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable("user_id") Long userId){
        User user = userService.findById(userId);
        return ResponseEntity.ok(new UserPublicDetailsRequestBody(user.getId(), user.getName(), user.getCreatedAt()));
    }
}
