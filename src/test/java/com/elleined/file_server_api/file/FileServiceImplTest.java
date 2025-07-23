package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import com.elleined.file_server_api.file.flattener.FileFlattener;
import com.elleined.file_server_api.file.util.FileUtil;
import com.elleined.file_server_api.folder.FolderService;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private FolderService folderService;

    @Mock
    private FileUtil fileUtil;

    @Mock
    private Tika tika;

    @Mock
    private FileFlattener fileFlattener;

    @InjectMocks
    private FileServiceImpl fileService;

    @TempDir
    private Path tempDir;

    private static Stream<Arguments> save_HappyPath_Payload() {
        return Stream.of(
                Arguments.of("image/png", "png"),
                Arguments.of("image/jpeg", "jpg"),
                Arguments.of("image/jpeg", "jpeg")
        );
    }

    private static Stream<Arguments> getByName_HappyPath_Payload() {
        return Stream.of(
                Arguments.of("image/png", "png"),
                Arguments.of("image/jpeg", "jpg"),
                Arguments.of("application/pdf", "pdf")
        );
    }

    @ParameterizedTest
    @MethodSource("save_HappyPath_Payload")
    void save_HappyPath_ImagePNG_AndJPG_AndJPEG(String mediaType, String extension) throws IOException, FileServerAPIException, MimeTypeException, NoSuchAlgorithmException {
        // Pre defined values

        // Expected Value
        String checksum = "checksum";
        Set<PosixFilePermission> expectedPermissions = Set.of(PosixFilePermission.OWNER_READ);


        // Mock data
        UUID folder = UUID.randomUUID();
        String fileName = UUID.randomUUID() + "." + extension;

        Path folderPath = tempDir.resolve(folder.toString()).normalize();
        Path filePath = folderPath.resolve(fileName).normalize();
        MultipartFile file = mock(MultipartFile.class);

        // Set up method
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        // Stubbing methods
        when(tika.detect(any(InputStream.class))).thenReturn(mediaType);
        when(fileUtil.getFileExtension(any(MediaType.class))).thenReturn(extension);
        when(fileUtil.getFileName(any(UUID.class), anyString())).thenReturn(fileName);
        when(folderService.getByName(any(UUID.class))).thenReturn(folderPath);
        when(fileUtil.resolve(any(Path.class), anyString())).thenReturn(filePath);
        doAnswer(answer -> {
            Files.createDirectory(folderPath);
            Files.createFile(filePath);
            return answer;
        }).when(fileFlattener).flattenImage(any(Path.class), any(MultipartFile.class), anyString());
        when(fileUtil.checksum(any(Path.class))).thenReturn(checksum);

        // Calling the method
        FileDTO fileDTO = assertDoesNotThrow(() -> fileService.save(folder, file));

        // Behavior Verifications
        verify(tika).detect(any(InputStream.class));
        verify(fileUtil).getFileExtension(any(MediaType.class));
        verify(fileUtil).getFileName(any(UUID.class), anyString());
        verify(folderService).getByName(any(UUID.class));
        verify(fileUtil).resolve(any(Path.class), anyString());
        verify(fileFlattener).flattenImage(any(Path.class), any(MultipartFile.class), anyString());
        verify(fileUtil).checksum(any(Path.class));
        verifyNoMoreInteractions(fileFlattener);

        // Assertions
        assertNotNull(fileDTO);

        assertNotNull(fileDTO.uploadedAt());

        assertNotNull(fileDTO.folder());
        assertEquals(folder, fileDTO.folder());

        assertNotNull(fileDTO.fileId());

        assertNotNull(fileDTO.extension());
        assertEquals(extension, fileDTO.extension());

        assertNotNull(fileDTO.mediaType());
        assertEquals(mediaType, fileDTO.mediaType());

        assertNotNull(fileDTO.checksum());
        assertEquals(checksum, fileDTO.checksum());

        assertNotNull(fileDTO.getFileName());

        assertEquals(expectedPermissions, Files.getPosixFilePermissions(filePath, LinkOption.NOFOLLOW_LINKS));
    }

    @ParameterizedTest
    @MethodSource("getByName_HappyPath_Payload")
    void getByName_HappyPath(String mediaType, String extension) throws FileServerAPIException, IOException, MimeTypeException {
        // Pre defined values

        // Expected Value

        // Mock data
        UUID file = UUID.randomUUID();

        Path expectedFilePath = tempDir.resolve(file.toString());
        String expectedFileName = file + "." + extension;

        // Set up method
        Files.createDirectory(expectedFilePath);

        // Stubbing methods
        when(folderService.getByName(any(UUID.class))).thenReturn(tempDir);
        when(tika.detect(any(Path.class))).thenReturn(mediaType);
        when(fileUtil.getFileExtension(any(MediaType.class))).thenReturn(extension);

        // Calling the method
        FileEntity fileEntity = assertDoesNotThrow(() -> fileService.getByName(UUID.randomUUID(), file));

        // Behavior Verifications
        verify(folderService).getByName(any(UUID.class));
        verify(tika).detect(any(Path.class));
        verify(fileUtil).getFileExtension(any(MediaType.class));

        // Assertions
        assertNotNull(fileEntity);

        assertNotNull(fileEntity.filePath());
        assertEquals(expectedFilePath, fileEntity.filePath());

        assertNotNull(fileEntity.fileId());
        assertEquals(file, fileEntity.fileId());

        assertNotNull(fileEntity.mediaType());
        assertEquals(mediaType, fileEntity.mediaType());

        assertNotNull(fileEntity.extension());
        assertEquals(extension, fileEntity.extension());

        assertNotNull(fileEntity.getFileName());
        assertEquals(expectedFileName, fileEntity.getFileName());
    }
}