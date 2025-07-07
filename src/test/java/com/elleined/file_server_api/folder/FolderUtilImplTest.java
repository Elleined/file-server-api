package com.elleined.file_server_api.folder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FolderUtilImplTest {

    @InjectMocks
    private FolderUtilImpl folderUtil;

    @TempDir
    private Path tempDir;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(folderUtil, "uploadPath", tempDir.toString());
    }

    @Test
    void getUploadPath_HappyPath() {
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
    void getUploadPath_ShouldHave700FolderPermission() throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data
        Set<PosixFilePermission> expectedPathPermissions = Set.of(
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_WRITE,
                PosixFilePermission.OWNER_EXECUTE
        );

        // Set up method

        // Stubbing methods

        // Calling the method
        Path actual = assertDoesNotThrow(() -> folderUtil.getUploadPath());

        // Behavior Verifications

        // Assertions
        assertEquals(expectedPathPermissions,
                Files.getPosixFilePermissions(actual, LinkOption.NOFOLLOW_LINKS),
                "Change the uploadPath folder permission to 700 or rwx------ to pass this test");
    }

    @Test
    void getUploadPath_ShouldThrowNoSuchFileException_IfUploadPathDoesNotExist() {
        ReflectionTestUtils.setField(folderUtil, "uploadPath", "/not/existing/path");

        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method
        assertThrowsExactly(NoSuchFileException.class, () -> folderUtil.getUploadPath());

        // Behavior Verifications

        // Assertions
    }
}