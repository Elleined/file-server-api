package com.elleined.file_server_api.folder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class FolderServiceImplTest {

    @InjectMocks
    private FolderServiceImpl folderService;

    private final static String uploadPath = "./src/test/resources";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(folderService, "uploadPath", uploadPath);
    }

    @Test
    void save_AndGet_AndRemove_HappyPath() {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method
        UUID folder = assertDoesNotThrow(() -> folderService.save());
        Path path = assertDoesNotThrow(() -> folderService.getByName(folder));
        assertDoesNotThrow(() -> folderService.deleteByName(folder));

        // Behavior Verifications

        // Assertions
        assertNotNull(folder);
        assertNotNull(path);
    }

    @Test
    void save_ShouldThrow_ForFolderLengthMoreThan37Characters() {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method

        // Behavior Verifications

        // Assertions
    }

    @Test
    void getUploadPath_HappyPath() throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data
        Path expected = Paths.get(uploadPath)
                .toRealPath(LinkOption.NOFOLLOW_LINKS);

        Set<PosixFilePermission> expectedPathPermissions = Set.of(
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_WRITE,
                PosixFilePermission.OWNER_EXECUTE
        );
        // Set up method

        // Stubbing methods

        // Calling the method
        Path actual = assertDoesNotThrow(() -> folderService.getUploadPath());

        Set<PosixFilePermission> actualPathPermissions = Files.getPosixFilePermissions(actual, LinkOption.NOFOLLOW_LINKS);
        // Behavior Verifications

        // Assertions
        assertEquals(expected, actual);
        assertEquals(expectedPathPermissions, actualPathPermissions, "Change the uploadPath folder permission to 700 or rwx------ to pass this test");
    }
}