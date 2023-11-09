package br.com.project.samuraicars.DTO.photo;

import java.util.List;

public record PhotosGetResponseBody(
        List<String> photos
) {
}
