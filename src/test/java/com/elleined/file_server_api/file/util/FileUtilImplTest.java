package com.elleined.file_server_api.file.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class FileUtilImplTest {

    @InjectMocks
    private FileUtilImpl fileUtil;
    @Test
    void getFileExtension_HappyPath() {
        // Pre defined values

        // Expected Value

        // Mock data
        UUID fileId = UUID.randomUUID();
        String extension = "extension";

        String expectedFileName = fileId + "." + extension;

        // Set up method

        // Stubbing methods
        String actual = assertDoesNotThrow(() -> fileUtil.getFileName(fileId, extension));

        // Calling the method

        // Behavior Verifications

        // Assertions
        assertThat(actual).isNotNull().isEqualTo(expectedFileName);
    }

    @Test
    void checksum_HappyPath(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data
        Path filePath = Files.createFile(tempDir.resolve(UUID.randomUUID().toString()));

        // Set up method

        // Stubbing methods

        // Calling the method
        String actual = assertDoesNotThrow(() -> fileUtil.checksum(filePath));

        // Behavior Verifications

        // Assertions
        assertThat(actual).isNotNull().isEqualTo("47DEQpj8HBSa-_TImW-5JCeuQeRkm5NMpJWZG3hSuFU");
    }
}