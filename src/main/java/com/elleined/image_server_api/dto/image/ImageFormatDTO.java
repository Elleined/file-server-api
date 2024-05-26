package com.elleined.image_server_api.dto.image;

import com.elleined.image_server_api.dto.DTO;
import com.elleined.image_server_api.dto.IntegerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ImageFormatDTO extends IntegerDTO {
    private String format;
}
