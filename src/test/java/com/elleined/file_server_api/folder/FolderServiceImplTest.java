package com.elleined.file_server_api.folder;

import com.elleined.file_server_api.exception.FileServerAPIException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FolderServiceImplTest {

    @Mock
    private FolderUtil folderUtil;

    @InjectMocks
    private FolderServiceImpl folderService;

    @Test
    void save_HappyPath(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method
        when(folderUtil.getUploadPath()).thenReturn(tempDir);
        when(folderUtil.isInUploadPath(any(Path.class))).thenReturn(true);

        // Stubbing methods

        // Calling the method
        UUID folder = assertDoesNotThrow(() -> folderService.save());

        // Behavior Verifications
        verify(folderUtil).getUploadPath();
        verify(folderUtil).isInUploadPath(any(Path.class));

        // Assertions
        assertNotNull(folder);
    }

    @Test
    void save_ShouldThrowFileServerException_IfNotInUploadPath(@TempDir Path tempDir) throws IOException {
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
}