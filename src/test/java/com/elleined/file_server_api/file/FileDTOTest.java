package com.elleined.file_server_api.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FileDTOTest {

    @Test
    void fileDTO_HappyPath() {
        // Pre defined values

        // Expected Value
        UUID folder = UUID.randomUUID();
        UUID file = UUID.randomUUID();
        String extension = "testExtension";
        MediaType mediaType = MediaType.IMAGE_PNG;
        String checksum = "testChecksum";
        String fileName = file + "." + extension;

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method
        FileDTO fileDTO = new FileDTO(folder, file, extension, mediaType, checksum);

        // Behavior Verifications

        // Assertions
        assertThat(fileDTO).isNotNull();

        assertThat(fileDTO.uploadedAt()).isNotNull();

        assertThat(folder).isEqualTo(fileDTO.folder());

        assertThat(file).isEqualTo(fileDTO.fileId());

        assertThat(extension).isEqualTo(fileDTO.extension());

        assertThat(mediaType.toString()).hasToString(fileDTO.mediaType());

        assertThat(checksum).isEqualTo(fileDTO.checksum());

        assertThat(fileDTO.getFileName()).isNotNull().isEqualTo(fileName);
    }
}