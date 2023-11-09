package br.com.project.samuraicars.DTO.vehicle;

import br.com.project.samuraicars.DTO.photo.PhotosGetResponseBody;

public record VehicleDetailsGetResponseBody(
        VehicleGetResponseBody vehicle,
        PhotosGetResponseBody images
) {
}
