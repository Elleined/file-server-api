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
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
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

    @Test
    void save_AndGet_AndRemove_HappyPath(@TempDir Path tempDir) throws IOException {
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
    void save_HappyPath(@TempDir Path tempDir) throws IOException {
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

    @Test
    void save_ShouldThrowFileServerException_IfFolderIsSymbolicLink(@TempDir Path tempDir) throws IOException {
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
    void save_ShouldThrowFileServerException_IfFolderAlreadyExists(@TempDir Path tempDir) throws IOException {
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
    void deleteByName_emptyFolder(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods
        when(folderUtil.getUploadPath()).thenReturn(tempDir);
        when(folderUtil.isInUploadPath(any(Path.class))).thenReturn(true);

        // Calling the method
        UUID emptyFolder = folderService.save();
        Path emptyFolderPath = tempDir.resolve(emptyFolder.toString());

        assertTrue(Files.exists(emptyFolderPath, LinkOption.NOFOLLOW_LINKS));
        assertDoesNotThrow(() -> folderService.deleteByName(emptyFolder));
        assertTrue(Files.notExists(emptyFolderPath, LinkOption.NOFOLLOW_LINKS));

        // Behavior Verifications
        verify(folderUtil, times(2)).getUploadPath();
        verify(folderUtil, times(2)).isInUploadPath(any(Path.class));

        // Assertions
    }

    @Test
    void deleteByName_folderWithFiles(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods
        when(folderUtil.getUploadPath()).thenReturn(tempDir);
        when(folderUtil.isInUploadPath(any(Path.class))).thenReturn(true);

        // Calling the method
        UUID tempFolder = folderService.save();
        Path tempFolderPath = tempDir.resolve(tempFolder.toString());
        Path tempFile = Files.createFile(tempFolderPath.resolve("file.txt"));

        assertTrue(Files.exists(tempFolderPath, LinkOption.NOFOLLOW_LINKS));
        assertTrue(Files.exists(tempFile, LinkOption.NOFOLLOW_LINKS));
        assertDoesNotThrow(() -> folderService.deleteByName(tempFolder));
        assertTrue(Files.notExists(tempFolderPath, LinkOption.NOFOLLOW_LINKS));
        assertTrue(Files.notExists(tempFile, LinkOption.NOFOLLOW_LINKS));

        // Behavior Verifications
        verify(folderUtil, times(2)).getUploadPath();
        verify(folderUtil, times(2)).isInUploadPath(any(Path.class));

        // Assertions
    }

    @Test
    void deleteByName_nestedFolderAndFiles(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods
        when(folderUtil.getUploadPath()).thenReturn(tempDir);
        when(folderUtil.isInUploadPath(any(Path.class))).thenReturn(true);

        // Calling the method
        UUID tempFolder = folderService.save();

        Path parentFolder = tempDir.resolve(tempFolder.toString());
        Path childFolder = Files.createDirectory(parentFolder.resolve("childFolder"));

        Path fileInsideParentFolder = Files.createFile(parentFolder.resolve("file.txt"));
        Path fileInsideChildFolder = Files.createFile(childFolder.resolve("file2.txt"));

        assertTrue(Files.exists(parentFolder, LinkOption.NOFOLLOW_LINKS));
        assertTrue(Files.exists(childFolder, LinkOption.NOFOLLOW_LINKS));

        assertTrue(Files.exists(fileInsideParentFolder, LinkOption.NOFOLLOW_LINKS));
        assertTrue(Files.exists(fileInsideChildFolder, LinkOption.NOFOLLOW_LINKS));

        assertDoesNotThrow(() -> folderService.deleteByName(tempFolder));

        assertTrue(Files.notExists(parentFolder, LinkOption.NOFOLLOW_LINKS));
        assertTrue(Files.notExists(childFolder, LinkOption.NOFOLLOW_LINKS));

        assertTrue(Files.notExists(fileInsideParentFolder, LinkOption.NOFOLLOW_LINKS));
        assertTrue(Files.notExists(fileInsideChildFolder, LinkOption.NOFOLLOW_LINKS));

        // Behavior Verifications
        verify(folderUtil, times(2)).getUploadPath();
        verify(folderUtil, times(2)).isInUploadPath(any(Path.class));

        // Assertions
    }

    @Test
    void deleteByName_ShouldThrowFileServerException_IfNotInUploadPath(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods
        when(folderUtil.getUploadPath()).thenReturn(tempDir);
        when(folderUtil.isInUploadPath(any(Path.class))).thenReturn(false);

        // Calling the method
        UUID tempFolder = UUID.randomUUID();
        Files.createDirectory(tempDir.resolve(tempFolder.toString()));

        assertThrowsExactly(FileServerAPIException.class, () -> folderService.deleteByName(tempFolder));

        // Behavior Verifications
        verify(folderUtil).getUploadPath();
        verify(folderUtil).isInUploadPath(any(Path.class));

        // Assertions
    }
}