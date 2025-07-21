package com.elleined.file_server_api.file;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertNotNull(fileEntity);

        assertNotNull(fileEntity.filePath());
        assertEquals(filePath, fileEntity.filePath());

        assertNotNull(fileEntity.fileId());
        assertEquals(fileId, fileEntity.fileId());

        assertNotNull(fileEntity.extension());
        assertEquals(extension, fileEntity.extension());

        assertNotNull(fileEntity.mediaType());
        assertEquals(mediaType, fileEntity.mediaType());
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
        assertNotNull(fileEntity.getContentDisposition());
        assertEquals("inline", fileEntity.getContentDisposition(), "Content disposition must be inline when media type starts with image/");
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
        assertNotNull(fileEntity.getContentDisposition());
        assertEquals("inline", fileEntity.getContentDisposition(), "Content disposition must be inline when media type starts with image/");
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
        assertNotNull(fileEntity.getContentDisposition());
        assertEquals("attachment", fileEntity.getContentDisposition(), "Content disposition must be attachment when media type is PDF");
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
        assertNotNull(fileEntity.getFileName());
        assertEquals(expectedFileName, fileEntity.getFileName());
    }
}