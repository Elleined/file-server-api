package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import com.elleined.file_server_api.folder.FolderService;
import org.apache.tika.Tika;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private FolderService folderService;

    @Spy
    private Tika tika;

    @InjectMocks
    private FileServiceImpl fileService;

    private final InputStream pdfFile = getClass().getClassLoader().getResourceAsStream("pdf.pdf");
    private final InputStream pngFile = getClass().getClassLoader().getResourceAsStream("png.png");
    private final InputStream jpegFile = getClass().getClassLoader().getResourceAsStream("jpeg.jpeg");
    private final InputStream jpgFile = getClass().getClassLoader().getResourceAsStream("jpg.jpg");

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fileService, "maxFileSize", DataSize.ofMegabytes(20));
    }

    @Test
    void save_HappyPath_ForPDF(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value
        MultipartFile file = new MockMultipartFile(
                "file",
                "pdf.pdf",
                "application/pdf",
                pdfFile);

        // Mock data
        UUID folder = UUID.randomUUID();
        Path folderPath = tempDir.resolve(folder.toString());
        Files.createDirectory(folderPath);

        // Set up method

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
        assertNotNull(fileDTO.name());
        assertEquals("pdf", fileDTO.extension());
        assertEquals("application/pdf", fileDTO.mimeType());
    }

    @Test
    void save_HappyPath_ForPNG(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value
        MultipartFile file = new MockMultipartFile(
                "file",
                "png.png",
                "image/png",
                pngFile);

        // Mock data
        UUID folder = UUID.randomUUID();
        Path folderPath = tempDir.resolve(folder.toString());
        Files.createDirectory(folderPath);

        // Set up method

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
        assertNotNull(fileDTO.name());
        assertEquals("png", fileDTO.extension());
        assertEquals("image/png", fileDTO.mimeType());
    }

    @Test
    void save_HappyPath_ForJPEG(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value
        MultipartFile file = new MockMultipartFile(
                "file",
                "jpeg.jpeg",
                "image/jpeg",
                jpegFile);

        // Mock data
        UUID folder = UUID.randomUUID();
        Path folderPath = tempDir.resolve(folder.toString());
        Files.createDirectory(folderPath);

        // Set up method

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
        assertNotNull(fileDTO.name());
        assertEquals("jpeg", fileDTO.extension());
        assertEquals("image/jpeg", fileDTO.mimeType());
    }

    @Test
    void save_HappyPath_ForJPG(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value
        MultipartFile file = new MockMultipartFile(
                "file",
                "jpg.jpg",
                "image/jpeg",
                jpgFile);

        // Mock data
        UUID folder = UUID.randomUUID();
        Path folderPath = tempDir.resolve(folder.toString());
        Files.createDirectory(folderPath);

        // Set up method

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
        assertNotNull(fileDTO.name());
        assertEquals("jpeg", fileDTO.extension());
        assertEquals("image/jpeg", fileDTO.mimeType());
    }


    @Test
    void save_ShouldThrowFileServerException_ForInvalidFileExtension(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value
        MultipartFile file = new MockMultipartFile(
                "file",
                "test-image.txt",
                "text/plain",
                "This is a text file".getBytes());

        // Mock data
        UUID folder = UUID.randomUUID();
        Path folderPath = tempDir.resolve(folder.toString());
        Files.createDirectory(folderPath);

        // Set up method

        // Stubbing methods

        // Calling the method
        assertThrowsExactly(FileServerAPIException.class, () -> fileService.save(folder, file));

        // Behavior Verifications
        verifyNoInteractions(folderService, tika);

        // Assertions
    }

    @Test
    void save_ShouldThrowFileServerException_ForFileSizeLimitExceeded(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value
        MultipartFile file = new MockMultipartFile(
                "file",
                "png.png",
                "application/png",
                new byte[(int) DataSize.ofMegabytes(21).toBytes()]);

        // Mock data
        UUID folder = UUID.randomUUID();
        Path folderPath = tempDir.resolve(folder.toString());
        Files.createDirectory(folderPath);

        // Set up method

        // Stubbing methods

        // Calling the method
        assertThrowsExactly(FileServerAPIException.class, () -> fileService.save(folder, file));

        // Behavior Verifications
        verifyNoInteractions(folderService, tika);

        // Assertions
    }


    @Test
    void save_ShouldThrowFileServerException_ForInvalidMimeType(@TempDir Path tempDir) throws IOException {
        // Pre defined values

        // Expected Value
        MultipartFile file = new MockMultipartFile(
                "file",
                "test-image.txt",
                "text/plain",
                "This is a text file".getBytes());

        // Mock data
        UUID folder = UUID.randomUUID();
        Path folderPath = tempDir.resolve(folder.toString());
        Files.createDirectory(folderPath);

        // Set up method

        // Stubbing methods

        // Calling the method
        assertThrowsExactly(FileServerAPIException.class, () -> fileService.save(folder, file));

        // Behavior Verifications
        verifyNoInteractions(folderService, tika);

        // Assertions
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