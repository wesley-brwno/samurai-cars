package br.com.project.samuraicars.utils;

import br.com.project.samuraicars.DTO.contactMessage.ContactMessageRequestBody;

public class ContactMessageRequestBodyCreator {
    public static ContactMessageRequestBody createValidContactMessageRequestBody() {
        return new ContactMessageRequestBody(
                "Tom",
                "cat",
                "12345678910",
                "tomcat@email.com",
                "Hello, this is a nice car, so how can I buy it?",
                1L
        );
    }
}
