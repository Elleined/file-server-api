package com.elleined.file_server_api.folder;

import com.elleined.file_server_api.exception.FileServerAPIException;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FolderServiceImplTest {

    @Mock
    private FolderUtil folderUtil;

    @InjectMocks
    private FolderServiceImpl folderService;

    @TempDir
    private Path tempDir;

    @Test
    void save_AndGet_AndRemove_HappyPath() throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method
        when(folderUtil.getUploadPath()).thenReturn(tempDir);
        when(folderUtil.isInUploadPath(any(Path.class))).thenReturn(true);
        when(folderUtil.isSymbolicLink(any(Path.class))).thenReturn(false);
        when(folderUtil.exists(any(Path.class))).thenReturn(false);

        // Stubbing methods

        // Calling the method
        UUID folder = assertDoesNotThrow(() -> folderService.save());
        Path path = assertDoesNotThrow(() -> folderService.getByName(folder));
        assertDoesNotThrow(() -> folderService.deleteByName(folder));

        // Behavior Verifications
        verify(folderUtil, times(3)).getUploadPath();
        verify(folderUtil, times(3)).isInUploadPath(any(Path.class));
        verify(folderUtil).isSymbolicLink(any(Path.class));
        verify(folderUtil).exists(any(Path.class));

        // Assertions
        assertNotNull(folder);
        assertNotNull(path);
    }

    @Test
    void save_HappyPath() throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method
        when(folderUtil.getUploadPath()).thenReturn(tempDir);
        when(folderUtil.isInUploadPath(any(Path.class))).thenReturn(true);
        when(folderUtil.isSymbolicLink(any(Path.class))).thenReturn(false);
        when(folderUtil.exists(any(Path.class))).thenReturn(false);

        // Stubbing methods

        // Calling the method
        UUID folder = assertDoesNotThrow(() -> folderService.save());

        // Behavior Verifications
        verify(folderUtil).getUploadPath();
        verify(folderUtil).isInUploadPath(any(Path.class));
        verify(folderUtil).isSymbolicLink(any(Path.class));
        verify(folderUtil).exists(any(Path.class));

        // Assertions
        assertNotNull(folder);
    }

    @Test
    void save_ShouldThrowFileServerException_IfNotInUploadPath() throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method
        when(folderUtil.getUploadPath()).thenReturn(tempDir);
        when(folderUtil.isInUploadPath(any(Path.class))).thenReturn(false);

        // Stubbing methods

        // Calling the method
        assertThrowsExactly(FileServerAPIException.class, () -> folderService.save());

        // Behavior Verifications
        verify(folderUtil).getUploadPath();
        verify(folderUtil).isInUploadPath(any(Path.class));

        // Assertions
    }

    @Test
    void save_ShouldThrowFileServerException_IfFolderIsSymbolicLink() throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method
        when(folderUtil.getUploadPath()).thenReturn(tempDir);
        when(folderUtil.isInUploadPath(any(Path.class))).thenReturn(true);
        when(folderUtil.isSymbolicLink(any(Path.class))).thenReturn(true);

        // Stubbing methods

        // Calling the method
        assertThrowsExactly(FileServerAPIException.class, () -> folderService.save());

        // Behavior Verifications
        verify(folderUtil).getUploadPath();
        verify(folderUtil).isInUploadPath(any(Path.class));
        verify(folderUtil).isSymbolicLink(any(Path.class));

        // Assertions
    }

    @Test
    void save_ShouldThrowFileServerException_IfFolderAlreadyExists() throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method
        when(folderUtil.getUploadPath()).thenReturn(tempDir);
        when(folderUtil.isInUploadPath(any(Path.class))).thenReturn(true);
        when(folderUtil.isSymbolicLink(any(Path.class))).thenReturn(false);
        when(folderUtil.exists(any(Path.class))).thenReturn(true);

        // Stubbing methods

        // Calling the method
        assertThrowsExactly(FileServerAPIException.class, () -> folderService.save());

        // Behavior Verifications
        verify(folderUtil).getUploadPath();
        verify(folderUtil).isInUploadPath(any(Path.class));
        verify(folderUtil).isSymbolicLink(any(Path.class));
        verify(folderUtil).exists(any(Path.class));

        // Assertions
    }

    @Test
    void deleteByName_HappyPath() throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods
        when(folderUtil.getUploadPath()).thenReturn(tempDir);
        when(folderUtil.isInUploadPath(any(Path.class))).thenReturn(true);

        // Calling the method
        UUID folder = UUID.randomUUID();
        Path folderPath = tempDir.resolve(folder.toString());
        Files.createDirectory(folderPath);
        assertTrue(Files.exists(folderPath, LinkOption.NOFOLLOW_LINKS));


        assertDoesNotThrow(() -> folderService.deleteByName(folder));

        // Behavior Verifications
        verify(folderUtil).getUploadPath();
        verify(folderUtil).isInUploadPath(any(Path.class));

        // Assertions
        assertFalse(Files.exists(folderPath, LinkOption.NOFOLLOW_LINKS));
    }
}