package com.elleined.image_server_api.dto.image;

import com.elleined.image_server_api.dto.DTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ImageFormatDTO extends DTO {
    private String format;
}
