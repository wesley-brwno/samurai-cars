package br.com.project.samuraicars.utils;

import br.com.project.samuraicars.model.ContactMessage;
import br.com.project.samuraicars.model.User;

public class ContactMessageCreator {
    public static ContactMessage createValidContactMessage() {
        return new ContactMessage(
                1L,
                "Tom",
                "cat",
                "tomcat@email.com",
                "12345678910",
                "Hello, this is a nice car, so how can I buy it?",
                false,
                1L,
                new User()
        );
    }
}
