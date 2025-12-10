package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import com.elleined.file_server_api.file.flattener.FileFlattener;
import com.elleined.file_server_api.file.util.FileUtil;
import com.elleined.file_server_api.folder.FolderService;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.junit.jupiter.api.Test;
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
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
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

    private static Stream<Arguments> getByUUID_HappyPath_Payload() {
        return Stream.of(
                Arguments.of("image/png", "png"),
                Arguments.of("image/jpeg", "jpg"),
                Arguments.of("image/jpeg", "jpeg"),
                Arguments.of("application/pdf", "pdf")
        );
    }


    @ParameterizedTest
    @MethodSource("save_HappyPath_Payload")
    void save_HappyPath_ImagePNG_AndJPG_AndJPEG(String mediaType, String extension) throws IOException, MimeTypeException, NoSuchAlgorithmException {
        // Pre defined values

        // Expected Value
        String checksum = "checksum";

        // Mock data
        MultipartFile file = mock(MultipartFile.class);

        UUID folder = UUID.randomUUID();
        String fileName = UUID.randomUUID() + "." + extension;

        Path folderPath = tempDir.resolve(folder.toString());
        Path filePath = folderPath.resolve(fileName);

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
        assertThat(fileDTO).isNotNull();

        assertThat(fileDTO.uploadedAt()).isNotNull();

        assertThat(fileDTO.folder()).isNotNull().isEqualTo(folder);

        assertThat(fileDTO.fileId()).isNotNull();

        assertThat(fileDTO.extension()).isNotNull().isEqualTo(extension);

        assertThat(fileDTO.mediaType()).isNotNull().isEqualTo(mediaType);

        assertThat(fileDTO.checksum()).isNotNull().isEqualTo(checksum);

        assertThat(fileDTO.getFileName()).isNotNull();
    }

    @Test
    void save_HappyPath_PDF() throws IOException, MimeTypeException, NoSuchAlgorithmException {

        // Pre defined values

        // Expected Value
        String checksum = "checksum";

        // Mock data
        String extension = "pdf";
        String mediaType = "application/pdf";
        UUID folder = UUID.randomUUID();
        String fileName = UUID.randomUUID() + "." + extension;

        Path folderPath = tempDir.resolve(folder.toString()).normalize();
        Files.createDirectory(folderPath);

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
        when(fileUtil.checksum(any(Path.class))).thenReturn(checksum);

        // Calling the method
        FileDTO fileDTO = assertDoesNotThrow(() -> fileService.save(folder, file));

        // Behavior Verifications
        verify(tika).detect(any(InputStream.class));
        verify(fileUtil).getFileExtension(any(MediaType.class));
        verify(fileUtil).getFileName(any(UUID.class), anyString());
        verify(folderService).getByName(any(UUID.class));
        verify(fileUtil).resolve(any(Path.class), anyString());
        verify(fileUtil).checksum(any(Path.class));

        // Assertions
        assertThat(fileDTO).isNotNull();

        assertThat(fileDTO.uploadedAt()).isNotNull();

        assertThat(fileDTO.folder()).isNotNull().isEqualTo(folder);

        assertThat(fileDTO.fileId()).isNotNull();

        assertThat(fileDTO.extension()).isNotNull().isEqualTo(extension);

        assertThat(fileDTO.mediaType()).isNotNull().isEqualTo(mediaType);

        assertThat(fileDTO.checksum()).isNotNull().isEqualTo(checksum);

        assertThat(fileDTO.getFileName()).isNotNull();
    }

    @Test
    void save_ShouldThrowFileServerAPIException_IfMediaTypeIsNotPNG_JPEG_OrPDF() throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data
        UUID folder = UUID.randomUUID();
        MultipartFile file = mock(MultipartFile.class);

        // Set up method
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        // Stubbing methods
        when(tika.detect(any(InputStream.class))).thenReturn(MediaType.APPLICATION_XML.toString());

        // Calling the method
        assertThrowsExactly(FileServerAPIException.class, () -> fileService.save(folder, file));

        // Behavior Verifications
        verify(tika).detect(any(InputStream.class));
        verifyNoInteractions(fileUtil, folderService);

        // Assertions
    }

    @ParameterizedTest
    @MethodSource("getByUUID_HappyPath_Payload")
    void getByUUID_HappyPath(String mediaType, String extension) throws IOException, MimeTypeException {
        // Pre defined values

        // Expected Value

        // Mock data
        UUID folder = UUID.randomUUID();
        UUID file = UUID.randomUUID();

        Path folderPath = tempDir.resolve(folder.toString());

        String expectedFileName = file + "." + extension;
        Path expectedFilePath = folderPath.resolve(expectedFileName);

        // Set up method
        Files.createDirectory(folderPath);
        Files.createDirectory(expectedFilePath);

        // Stubbing methods
        when(folderService.getByName(any(UUID.class))).thenReturn(folderPath);
        when(tika.detect(any(Path.class))).thenReturn(mediaType);
        when(fileUtil.getFileExtension(any(MediaType.class))).thenReturn(extension);

        // Calling the method
        FileEntity fileEntity = assertDoesNotThrow(() -> fileService.getByUUID(folder, file).orElseThrow());

        // Behavior Verifications
        verify(folderService).getByName(any(UUID.class));
        verify(tika).detect(any(Path.class));
        verify(fileUtil).getFileExtension(any(MediaType.class));

        // Assertions
        assertThat(fileEntity).isNotNull();

        assertThat(fileEntity.filePath()).isNotNull().isEqualTo(expectedFilePath);

        assertThat(fileEntity.fileId()).isNotNull().isEqualTo(file);

        assertThat(fileEntity.mediaType()).isNotNull().isEqualTo(mediaType);

        assertThat(fileEntity.extension()).isNotNull().isEqualTo(extension);

        assertThat(fileEntity.getFileName()).isNotNull().isEqualTo(expectedFileName);
    }
}