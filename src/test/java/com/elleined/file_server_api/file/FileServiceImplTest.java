package com.elleined.file_server_api.file;

import com.elleined.file_server_api.file.flattener.FileFlattener;
import com.elleined.file_server_api.file.util.FileUtil;
import com.elleined.file_server_api.folder.FolderService;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private FolderService folderService;

    private final DataSize maxFileSize = DataSize.ofMegabytes(20);
    @Mock
    private FileUtil fileUtil;

    @Spy
    private Tika tika;

    @InjectMocks
    private FileServiceImpl fileService;

    private final InputStream pdfFile = getClass().getClassLoader().getResourceAsStream("pdf.pdf");
    private final InputStream pngFile = getClass().getClassLoader().getResourceAsStream("png.png");
    private final InputStream jpegFile = getClass().getClassLoader().getResourceAsStream("jpeg.jpeg");
    private final InputStream jpgFile = getClass().getClassLoader().getResourceAsStream("jpg.jpg");
    @Mock
    private FileFlattener fileFlattener;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fileService, "maxFileSize", maxFileSize);
    }

    @Test
    void save_HappyPath_ForPDF(@TempDir Path tempDir) throws IOException, MimeTypeException {
        // Pre defined values

        // Expected Value
        MediaType expectedMediaType = MediaType.APPLICATION_PDF;
        String expectedExtension = MimeTypes.getDefaultMimeTypes()
                .forName(expectedMediaType.toString())
                .getExtension()
                .substring(1); // Remove the leading dot

        // Mock data
        MultipartFile file = new MockMultipartFile("file", pdfFile);

        // Set up method
        UUID folder = UUID.randomUUID();
        Path folderPath = tempDir.resolve(folder.toString());
        Files.createDirectory(folderPath);

        // Stubbing methods
        when(folderService.getByName(any(UUID.class))).thenReturn(folderPath);

        // Calling the method
        FileDTO fileDTO = assertDoesNotThrow(() -> fileService.save(folder, file));

        // Behavior Verifications
        verify(folderService).getByName(any(UUID.class));

        // Assertions
        assertNotNull(fileDTO);
        assertNotNull(fileDTO.uploadedAt());
        assertEquals(folder, fileDTO.folder());
        assertNotNull(fileDTO.fileId());
        assertEquals(expectedExtension, fileDTO.extension());
        assertEquals(expectedMediaType, fileDTO.mediaType());
    }

    @Test
    void save_HappyPath_ForPNG(@TempDir Path tempDir) throws IOException, MimeTypeException {
        // Pre defined values

        // Expected Value
        MediaType expectedMediaType = MediaType.IMAGE_PNG;
        String expectedExtension = MimeTypes.getDefaultMimeTypes()
                .forName(expectedMediaType.toString())
                .getExtension()
                .substring(1); // Remove the leading dot

        // Mock data
        MultipartFile file = new MockMultipartFile("file", pngFile);

        // Set up method
        UUID folder = UUID.randomUUID();
        Path folderPath = tempDir.resolve(folder.toString());
        Files.createDirectory(folderPath);

        // Stubbing methods
        when(folderService.getByName(any(UUID.class))).thenReturn(folderPath);

        // Calling the method
        FileDTO fileDTO = assertDoesNotThrow(() -> fileService.save(folder, file));

        // Behavior Verifications
        verify(folderService).getByName(any(UUID.class));

        // Assertions
        assertNotNull(fileDTO);
        assertNotNull(fileDTO.uploadedAt());
        assertEquals(folder, fileDTO.folder());
        assertNotNull(fileDTO.fileId());
        assertEquals(expectedExtension, fileDTO.extension());
        assertEquals(expectedMediaType, fileDTO.mediaType());
    }

    @Test
    void save_HappyPath_ForJPEG(@TempDir Path tempDir) throws IOException, MimeTypeException {
        // Pre defined values

        // Expected Value
        MediaType expectedMediaType = MediaType.IMAGE_JPEG;
        String expectedExtension = MimeTypes.getDefaultMimeTypes()
                .forName(expectedMediaType.toString())
                .getExtension()
                .substring(1); // Remove the leading dot

        // Mock data
        MultipartFile file = new MockMultipartFile("file", jpegFile);

        // Set up method
        UUID folder = UUID.randomUUID();
        Path folderPath = tempDir.resolve(folder.toString());
        Files.createDirectory(folderPath);

        // Stubbing methods
        when(folderService.getByName(any(UUID.class))).thenReturn(folderPath);

        // Calling the method
        FileDTO fileDTO = assertDoesNotThrow(() -> fileService.save(folder, file));

        // Behavior Verifications
        verify(folderService).getByName(any(UUID.class));

        // Assertions
        assertNotNull(fileDTO);
        assertNotNull(fileDTO.uploadedAt());
        assertEquals(folder, fileDTO.folder());
        assertNotNull(fileDTO.fileId());
        assertEquals(expectedExtension, fileDTO.extension());
        assertEquals(expectedMediaType, fileDTO.mediaType());
    }

    @Test
    void save_HappyPath_ForJPG(@TempDir Path tempDir) throws IOException, MimeTypeException {
        // Pre defined values

        // Expected Value
        MediaType expectedMediaType = MediaType.IMAGE_JPEG;
        String expectedExtension = MimeTypes.getDefaultMimeTypes()
                .forName(expectedMediaType.toString())
                .getExtension()
                .substring(1); // Remove the leading dot

        // Mock data
        MultipartFile file = new MockMultipartFile("file", jpgFile);

        // Set up method
        UUID folder = UUID.randomUUID();
        Path folderPath = tempDir.resolve(folder.toString());
        Files.createDirectory(folderPath);

        // Stubbing methods
        when(folderService.getByName(any(UUID.class))).thenReturn(folderPath);

        // Calling the method
        FileDTO fileDTO = assertDoesNotThrow(() -> fileService.save(folder, file));

        // Behavior Verifications
        verify(folderService).getByName(any(UUID.class));

        // Assertions
        assertNotNull(fileDTO);
        assertNotNull(fileDTO.uploadedAt());
        assertEquals(folder, fileDTO.folder());
        assertNotNull(fileDTO.fileId());
        assertEquals(expectedExtension, fileDTO.extension());
        assertEquals(expectedMediaType, fileDTO.mediaType());
    }

    @Test
    void getByName_HappyPath() {
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
    void isChecksumMatched_HappyPath() {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method

        // Behavior Verifications

        // Assertions
    }
}