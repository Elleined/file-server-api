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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
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

    private final InputStream pdfFile = getClass().getClassLoader().getResourceAsStream("pdf.pdf");
    private final InputStream pngFile = getClass().getClassLoader().getResourceAsStream("png.png");
    private final InputStream jpegFile = getClass().getClassLoader().getResourceAsStream("jpeg.jpeg");
    private final InputStream jpgFile = getClass().getClassLoader().getResourceAsStream("jpg.jpg");

    private static Stream<Arguments> getByName_MediaType_AndCorrespondingExtension() {
        return Stream.of(
                Arguments.of("image/png", "png"),
                Arguments.of("image/jpeg", "jpg"),
                Arguments.of("application/pdf", "pdf")
        );
    }

    @Test
    void save_PNG_HappyPath(@TempDir Path tempDir) throws IOException, FileServerAPIException, MimeTypeException, NoSuchAlgorithmException {
        // Pre defined values

        // Expected Value

        // Mock data
        String checksum = "checksum";
        UUID folder = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", pngFile);

        // Set up method

        // Stubbing methods
        when(tika.detect(any(InputStream.class))).thenReturn("image/png");
        when(fileUtil.getFileExtension(any(MediaType.class))).thenReturn("png");
        when(folderService.getByName(any(UUID.class))).thenReturn(tempDir);
        doNothing().when(fileFlattener).flattenImage(any(Path.class), any(MultipartFile.class), anyString());
        when(fileUtil.checksum(any(Path.class))).thenReturn(checksum);

        // Calling the method
        FileDTO fileDTO = assertDoesNotThrow(() -> fileService.save(folder, file));

        // Behavior Verifications
        verify(tika).detect(any(InputStream.class));
        verify(fileUtil).getFileExtension(any(MediaType.class));
        verify(folderService).getByName(any(UUID.class));
        verify(fileFlattener).flattenImage(any(Path.class), any(MultipartFile.class), anyString());
        verify(fileUtil).checksum(any(Path.class));
        verifyNoMoreInteractions(fileFlattener);

        // Assertions
        assertNotNull(fileDTO);
    }

    @ParameterizedTest
    @MethodSource("getByName_MediaType_AndCorrespondingExtension")
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