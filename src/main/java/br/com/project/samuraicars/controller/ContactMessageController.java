package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.DTO.contactMessage.ContactMessageRequestBody;
import br.com.project.samuraicars.DTO.contactMessage.ContactMessageResponseBody;
import br.com.project.samuraicars.model.ContactMessage;
import br.com.project.samuraicars.service.ContactMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class ContactMessageController {
    private final ContactMessageService contactMessageService;

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody ContactMessageRequestBody messageBody, UriComponentsBuilder uriBuilder) {
        ContactMessage savedMessage = contactMessageService.save(messageBody);
        URI uri = uriBuilder.path("/messages/{id}").buildAndExpand(savedMessage.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/{id}")
    public ResponseEntity<ContactMessageResponseBody> displayById(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok().body(contactMessageService.getById(id, user));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping()
    public ResponseEntity<Page<ContactMessageResponseBody>> displayByUser(@PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable,
                                                                          @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok().body(contactMessageService.findByUserPageable(pageable, user));
    }

    @Operation(security = { @SecurityRequirement(name = "bearer-key") })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        contactMessageService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
