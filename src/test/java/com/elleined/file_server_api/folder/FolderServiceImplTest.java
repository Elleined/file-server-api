package com.elleined.file_server_api.folder;

import com.elleined.file_server_api.folder.util.FolderUtil;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FolderServiceImplTest {

    @Mock
    private FolderUtil folderUtil;


    @InjectMocks
    private FolderServiceImpl folderService;

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
        assertThat(folder).isNotNull();
        assertThat(folderPath).exists().isDirectory();
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
        assertThat(expectedPath).exists().isDirectory();
        assertThat(actualPath).exists().isDirectory().isEqualTo(expectedPath.toRealPath(LinkOption.NOFOLLOW_LINKS));
    }
}