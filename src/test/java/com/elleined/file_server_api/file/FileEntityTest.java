package com.elleined.file_server_api.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FileEntityTest {

    @Test
    void fileMetaData_HappyPath() {
        // Pre defined values

        // Expected Value
        Path filePath = Paths.get("/path/to/file");
        UUID fileId = UUID.randomUUID();
        String extension = "extension";
        String mediaType = "mediaType";

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method
        FileEntity fileEntity = new FileEntity(filePath, fileId, extension, mediaType);

        // Behavior Verifications

        // Assertions
        assertThat(fileEntity).isNotNull();

        assertThat(fileEntity.filePath()).isNotNull().isEqualTo(filePath);

        assertThat(fileEntity.fileId()).isNotNull().isEqualTo(fileId);

        assertThat(fileEntity.extension()).isNotNull().isEqualTo(extension);

        assertThat(fileEntity.mediaType()).isNotNull().isEqualTo(mediaType);
    }

    @Test
    void getContentDisposition_InlinePNG_HappyPath() {
        // Pre defined values

        // Expected Value
        Path filePath = Paths.get("/path/to/file");
        UUID fileId = UUID.randomUUID();
        String extension = "extension";
        MediaType mediaType = MediaType.IMAGE_PNG;

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method
        FileEntity fileEntity = new FileEntity(filePath, fileId, extension, mediaType);

        // Behavior Verifications

        // Assertions
        assertThat(fileEntity.getContentDisposition()).isNotNull().isEqualTo("inline");
    }

    @Test
    void getContentDisposition_InlineJPEG_HappyPath() {
        // Pre defined values

        // Expected Value
        Path filePath = Paths.get("/path/to/file");
        UUID fileId = UUID.randomUUID();
        String extension = "extension";
        MediaType mediaType = MediaType.IMAGE_JPEG;

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method
        FileEntity fileEntity = new FileEntity(filePath, fileId, extension, mediaType);

        // Behavior Verifications

        // Assertions
        assertThat(fileEntity.getContentDisposition()).isNotNull().isEqualTo("inline");
    }

    @Test
    void getContentDisposition_AttachmentPDF_HappyPath() {
        // Pre defined values

        // Expected Value
        Path filePath = Paths.get("/path/to/file");
        UUID fileId = UUID.randomUUID();
        String extension = "extension";
        MediaType mediaType = MediaType.APPLICATION_PDF;

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method
        FileEntity fileEntity = new FileEntity(filePath, fileId, extension, mediaType);

        // Behavior Verifications

        // Assertions
        assertThat(fileEntity.getContentDisposition()).isNotNull().isEqualTo("attachment");
    }

    @Test
    void getFileName_HappyPath() {
        // Pre defined values

        // Expected Value
        Path filePath = Paths.get("/path/to/file");
        UUID fileId = UUID.randomUUID();
        String extension = "extension";
        MediaType mediaType = MediaType.IMAGE_PNG;

        String expectedFileName = fileId + "." + extension;

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method
        FileEntity fileEntity = new FileEntity(filePath, fileId, extension, mediaType);

        // Behavior Verifications

        // Assertions
        assertThat(fileEntity.getFileName()).isNotNull().isEqualTo(expectedFileName);
    }
}