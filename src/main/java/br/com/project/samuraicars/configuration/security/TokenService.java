package br.com.project.samuraicars.configuration.security;

import br.com.project.samuraicars.model.User;

public interface TokenService {
    String generateToken(User user);
    String getSubject(String token);
}
