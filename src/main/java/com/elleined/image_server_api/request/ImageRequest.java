package com.elleined.image_server_api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ImageRequest extends Request {
    @NotBlank(message = "Please provide the uuid. This will be your key access the image.")
    private String uuid;

    private String description;
    private String additionalInformation;

    @Positive(message = "Please provide the image format")
    private int imageFormatId;

    @Positive(message = "Please provide the project id you want this image to be saved.")
    private int projectId;

    // image is in separate @RequestPart
}
