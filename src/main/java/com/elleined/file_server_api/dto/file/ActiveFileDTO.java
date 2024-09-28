package com.elleined.file_server_api.dto.file;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ActiveFileDTO extends ImageDTO {
    private byte[] bytes;
}
