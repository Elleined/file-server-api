package com.elleined.image_server_api.dto.image;

import com.elleined.image_server_api.dto.UUIDDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ImageDTO extends UUIDDTO {
    private LocalDateTime lastAccessedAt;
    private String description;
    private String additionalInformation;
    private int imageFormatId;
}
