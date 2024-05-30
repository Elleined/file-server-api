package com.elleined.image_server_api.dto.format;

import com.elleined.image_server_api.dto.IntegerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FormatDTO extends IntegerDTO {
    private String format;
}
