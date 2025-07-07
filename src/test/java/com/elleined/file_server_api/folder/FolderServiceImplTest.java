package com.elleined.file_server_api.folder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

        // Stubbing methods

        // Calling the method
        UUID folder = assertDoesNotThrow(() -> folderService.save());

        // Behavior Verifications
        verify(folderUtil).getUploadPath();

        // Assertions
        assertNotNull(folder);
    }
}