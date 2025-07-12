package com.elleined.file_server_api.folder;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FolderServiceImplTest {

    @Mock
    private FolderUtil folderUtil;

    @InjectMocks
    private FolderServiceImpl folderService;

    private static ExecutableValidator executableValidator;

    @BeforeAll
    static void setupAll() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        executableValidator = validator.forExecutables();
    }

    @Test
    void save_AndGet_HappyPath(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method
        when(folderUtil.getUploadPath()).thenReturn(tempDir);

        // Stubbing methods

        // Calling the method
        UUID folder = assertDoesNotThrow(() -> folderService.save());
        Path folderPath = assertDoesNotThrow(() -> folderService.getByName(folder));

        // Behavior Verifications
        verify(folderUtil, times(2)).getUploadPath();

        // Assertions
        assertNotNull(folder);
        assertTrue(Files.exists(folderPath, LinkOption.NOFOLLOW_LINKS));
    }

    @Test
    void save_ShouldHave700FolderPermission(@TempDir Path tempDir) throws IOException {
        // Pre defined values
        Set<PosixFilePermission> expectedPermissions = Set.of(
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_WRITE,
                PosixFilePermission.OWNER_EXECUTE
        );

        // Mock data
        when(folderUtil.getUploadPath()).thenReturn(tempDir);

        // Calling the method
        UUID folder = assertDoesNotThrow(() -> folderService.save());
        Path folderPath = assertDoesNotThrow(() -> folderService.getByName(folder));

        // Behavior Verifications
        verify(folderUtil, times(2)).getUploadPath();

        // Assertions
        assertNotNull(folder);

        assertTrue(Files.exists(folderPath, LinkOption.NOFOLLOW_LINKS));
        assertTrue(Files.isDirectory(folderPath, LinkOption.NOFOLLOW_LINKS));
        assertTrue(Files.isReadable(folderPath));
        assertTrue(Files.isWritable(folderPath));
        assertTrue(Files.isExecutable(folderPath));

        assertTrue(Files.isSameFile(folderPath, folderPath.toRealPath(LinkOption.NOFOLLOW_LINKS)));
        assertTrue(Files.isDirectory(folderPath.toRealPath(LinkOption.NOFOLLOW_LINKS), LinkOption.NOFOLLOW_LINKS));
        assertEquals(expectedPermissions, Files.getPosixFilePermissions(folderPath), "Folder should have 700 permissions");
    }

    @Test
    void getByName_HappyPath(@TempDir Path tempDir) throws IOException {
        // Pre defined values
        UUID folder = UUID.randomUUID();

        // Mock data
        Path expectedPath = tempDir.resolve(folder.toString());
        Files.createDirectory(expectedPath);

        when(folderUtil.getUploadPath()).thenReturn(tempDir);

        // Calling the method
        Path actualPath = assertDoesNotThrow(() -> folderService.getByName(folder));

        // Behavior Verifications
        verify(folderUtil).getUploadPath();

        // Assertions
        assertEquals(expectedPath.toRealPath(LinkOption.NOFOLLOW_LINKS), actualPath);

        assertTrue(Files.exists(expectedPath, LinkOption.NOFOLLOW_LINKS));
        assertTrue(Files.isDirectory(expectedPath, LinkOption.NOFOLLOW_LINKS));

        assertTrue(Files.exists(actualPath, LinkOption.NOFOLLOW_LINKS));
        assertTrue(Files.isDirectory(actualPath, LinkOption.NOFOLLOW_LINKS));
    }
}