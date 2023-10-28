package br.com.project.samuraicars.DTO.user;

import br.com.project.samuraicars.model.User;

public record UserResponseBody(
        Long id,
        String name,
        String email
) {
    public UserResponseBody(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}
