package com.elleined.file_server_api.file.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileUtilImplTest {

    private final InputStream pdfFile = getClass().getClassLoader().getResourceAsStream("pdf.pdf");
    private final InputStream pngFile = getClass().getClassLoader().getResourceAsStream("png.png");

    @InjectMocks
    private FileUtilImpl fileUtil;

    @Test
    void getFileExtension_PDF_HappyPath() {
        // Pre defined values

        // Expected Value
        String expectedExtension = "pdf";

        // Mock data
        MediaType mediaType = MediaType.APPLICATION_PDF;

        // Set up method

        // Stubbing methods
        String actual = assertDoesNotThrow(() -> fileUtil.getFileExtension(mediaType));

        // Calling the method

        // Behavior Verifications

        // Assertions
        assertEquals(expectedExtension, actual);
    }

    @Test
    void getFileExtension_PNG_HappyPath() {
        // Pre defined values

        // Expected Value
        String expectedExtension = "png";

        // Mock data
        MediaType mediaType = MediaType.IMAGE_PNG;

        // Set up method

        // Stubbing methods
        String actual = assertDoesNotThrow(() -> fileUtil.getFileExtension(mediaType));

        // Calling the method

        // Behavior Verifications

        // Assertions
        assertEquals(expectedExtension, actual);
    }

    @Test
    void getFileExtension_JPEG_HappyPath() {
        // Pre defined values

        // Expected Value
        String expectedExtension = "jpg";

        // Mock data
        MediaType mediaType = MediaType.IMAGE_JPEG;

        // Set up method

        // Stubbing methods
        String actual = assertDoesNotThrow(() -> fileUtil.getFileExtension(mediaType));

        // Calling the method

        // Behavior Verifications

        // Assertions
        assertEquals(expectedExtension, actual);
    }

    @Test
    void checksum_HappyPath(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data
        Path file = Files.createTempFile(tempDir, "pdf", ".pdf");
        String expected = "47DEQpj8HBSa-_TImW-5JCeuQeRkm5NMpJWZG3hSuFU";

        // Set up method

        // Stubbing methods

        // Calling the method
        String actual = assertDoesNotThrow(() -> fileUtil.checksum(file));

        // Behavior Verifications

        // Assertions
        assertEquals(expected, actual, "actual and expected checksum should match");
    }

    @Test
    void checksum_ShouldNotEqual_IfFileHasBeenModified(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data
        Path realFile = Files.createTempFile(tempDir, "pdf", ".pdf");
        Path modifiedFile = Files.write(realFile, "Hello World".getBytes());

        String realFileChecksum = "47DEQpj8HBSa-_TImW-5JCeuQeRkm5NMpJWZG3hSuFU";

        // Set up method

        // Stubbing methods

        // Calling the method
        String actual = assertDoesNotThrow(() -> fileUtil.checksum(modifiedFile));

        // Behavior Verifications

        // Assertions
        assertNotEquals(realFileChecksum, actual, "realFileChecksum should not match with actual because realFile has been modified");
    }

    @Test
    void stream_HappyPath(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data
        Path file = Files.createTempFile(tempDir, "pdf", ".pdf");

        // Set up method

        // Stubbing methods

        // Calling the method
        StreamingResponseBody response = assertDoesNotThrow(() -> fileUtil.stream(file));

        // Behavior Verifications

        // Assertions
        assertNotNull(response);
    }
}