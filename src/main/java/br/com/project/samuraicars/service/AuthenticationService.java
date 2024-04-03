package br.com.project.samuraicars.service;

import br.com.project.samuraicars.DTO.authentication.UserAuthorizationRequestBody;
import br.com.project.samuraicars.DTO.authentication.UserRegisterPostRequestBody;

public interface AuthenticationService {
    void register(UserRegisterPostRequestBody user);
    String authorize(UserAuthorizationRequestBody user);
}
