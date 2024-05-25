package com.elleined.image_server_api.request;

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
    private String description;
    private String additionalInformation;

    @Positive(message = "Please provide the image format")
    private int imageFormatId;
    // is in path variable projectId;

    // image is in separate @RequestPart
}
