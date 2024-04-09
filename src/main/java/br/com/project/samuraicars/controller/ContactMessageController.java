package br.com.project.samuraicars.controller;

import br.com.project.samuraicars.DTO.contactMessage.ContactMessageRequestBody;
import br.com.project.samuraicars.DTO.contactMessage.ContactMessageResponseBody;
import br.com.project.samuraicars.model.User;
import br.com.project.samuraicars.service.ContactMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
@Tag(name = "Contact Message")
public class ContactMessageController {
    private final ContactMessageService contactMessageService;

    @PostMapping
    @Operation(description = "Save messages sent by public users")
    public ResponseEntity<Void> save(@Valid @RequestBody ContactMessageRequestBody messageBody) {
        contactMessageService.save(messageBody);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(
            security = {@SecurityRequirement(name = "bearer-key")},
            description = "Retrieve a message by its id")
    public ResponseEntity<ContactMessageResponseBody> displayById(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok().body(contactMessageService.listById(id, user));
    }

    @GetMapping()
    @Operation(
            security = {@SecurityRequirement(name = "bearer-key")},
            description = "Retrieve messages in a pageable for an authenticated user")
    public ResponseEntity<Page<ContactMessageResponseBody>> displayByUser(
            @PageableDefault(sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok().body(contactMessageService.findByUserPageable(pageable, (User) user));
    }

    @DeleteMapping("/{id}")
    @Operation(
            security = {@SecurityRequirement(name = "bearer-key")},
            description = "Delete a message by its id")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        contactMessageService.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
