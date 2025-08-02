package com.elleined.file_server_api.folder.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FolderUtilImplTest {

    @InjectMocks
    private FolderUtilImpl folderUtil;

    @Test
    void getUploadPath_HappyPath(@TempDir Path tempDir) {
        ReflectionTestUtils.setField(folderUtil, "uploadPath", tempDir.toString());
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method
        Path actual = assertDoesNotThrow(() -> folderUtil.getUploadPath());

        // Behavior Verifications

        // Assertions
        assertEquals(tempDir, actual, "Upload path should be the same as the tempDir");
    }

    @Test
    void getUploadPath_ShouldThrowIOException_IfUploadPathDoesNotExist() {
        ReflectionTestUtils.setField(folderUtil, "uploadPath", "/not/existing/path");

        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method
        assertThrows(IOException.class, () -> folderUtil.getUploadPath());

        // Behavior Verifications

        // Assertions
    }
}