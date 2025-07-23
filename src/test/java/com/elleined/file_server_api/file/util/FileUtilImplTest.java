package com.elleined.file_server_api.file.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileUtilImplTest {

    @InjectMocks
    private FileUtilImpl fileUtil;

    private static Stream<Arguments> getFileExtension_HappyPath_Payload() {
        return Stream.of(
                Arguments.of(MediaType.IMAGE_PNG, "png"),
                Arguments.of(MediaType.IMAGE_JPEG, "jpg"),
                Arguments.of(MediaType.APPLICATION_PDF, "pdf")
        );
    }

    @ParameterizedTest
    @MethodSource("getFileExtension_HappyPath_Payload")
    void getFileExtension_HappyPath(MediaType mediaType, String extension) {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods
        String actual = assertDoesNotThrow(() -> fileUtil.getFileExtension(mediaType));

        // Calling the method

        // Behavior Verifications

        // Assertions
        assertEquals(extension, actual);
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

    @Test
    void getFileName_HappyPath() {
        // Pre defined values

        // Expected Value

        // Mock data
        UUID fileId = UUID.randomUUID();
        String extension = "extension";

        String expected = fileId + "." + extension;
        // Set up method

        // Stubbing methods

        // Calling the method
        String actual = assertDoesNotThrow(() -> fileUtil.getFileName(fileId, extension));

        // Behavior Verifications

        // Assertions
        assertEquals(expected, actual);
    }

    @Test
    void resolve_HappyPath(@TempDir Path tempDir) {
        // Pre defined values

        // Expected Value

        // Mock data
        String fileName = "fileName";

        Path expected = tempDir.resolve(fileName).normalize();
        // Set up method

        // Stubbing methods

        // Calling the method
        Path actual = assertDoesNotThrow(() -> fileUtil.resolve(tempDir, fileName));

        // Behavior Verifications

        // Assertions
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}