package com.elleined.file_server_api.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertNotNull(fileDTO);
        assertNotNull(fileDTO.uploadedAt());
        assertEquals(folder, fileDTO.folder());
        assertEquals(file, fileDTO.fileId());
        assertEquals(extension, fileDTO.extension());
        assertEquals(mediaType.toString(), fileDTO.mediaType());
        assertEquals(checksum, fileDTO.checksum());

        assertNotNull(fileDTO.getFileName());
        assertEquals(fileName, fileDTO.getFileName());
    }
}