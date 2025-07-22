package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import com.elleined.file_server_api.file.util.FileUtil;
import org.apache.tika.mime.MimeTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileController.class)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FileService fileService;

    @MockitoBean
    private FileUtil fileUtil;

    @Test
    void save_HappyPath() throws IOException, FileServerAPIException, MimeTypeException, NoSuchAlgorithmException {
        // Pre defined values

        // Expected Value

        // Mock data
        UUID folder = UUID.randomUUID();
        UUID fileId = UUID.randomUUID();
        String extension = "testExtension";
        MediaType mediaType = MediaType.IMAGE_PNG;
        String checksum = "testChecksum";

        MockMultipartFile file = new MockMultipartFile("file", getClass().getClassLoader().getResourceAsStream("pdf.pdf"));
        FileDTO fileDTO = new FileDTO(folder, fileId, extension, mediaType, checksum);

        // Set up method

        // Stubbing methods
        when(fileService.save(any(UUID.class), any(MultipartFile.class))).thenReturn(fileDTO);

        // Calling the method
        assertDoesNotThrow(() -> mockMvc.perform(multipart("/folders/{folder}/files", folder)
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uploadedAt").exists())
                .andExpect(jsonPath("$.folder").value(folder.toString()))
                .andExpect(jsonPath("$.fileId").value(fileId.toString()))
                .andExpect(jsonPath("$.extension").value(extension))
                .andExpect(jsonPath("$.mediaType").value(mediaType.toString()))
                .andExpect(jsonPath("$.checksum").value(checksum))
        );

        // Behavior Verifications
        verify(fileService).save(any(UUID.class), any(MultipartFile.class));

        // Assertions
    }

    @Test
    void getByName_InlinePNG_HappyPath() throws FileServerAPIException, MimeTypeException, IOException {
        // Pre defined values

        // Expected Value
        Path filePath = Paths.get("/path/to/file");
        UUID fileId = UUID.randomUUID();
        String extension = "extension";
        MediaType mediaType = MediaType.IMAGE_PNG;
        String fileName = fileId + "." + extension;

        // Mock data
        UUID folder = UUID.randomUUID();
        FileEntity fileEntity = new FileEntity(filePath, fileId, extension, mediaType);

        // Set up method

        // Stubbing methods
        when(fileService.getByName(any(UUID.class), any(UUID.class))).thenReturn(fileEntity);
        when(fileUtil.stream(any(Path.class))).thenReturn(outputStream -> {
        });

        // Calling the method
        assertDoesNotThrow(() -> mockMvc.perform(get("/folders/{folder}/files/{fileId}", folder, fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(header().string(
                        HttpHeaders.CONTENT_DISPOSITION,
                        String.format("inline; filename=\"%s\"", fileName)
                ))
        );

        // Behavior Verifications
        verify(fileService).getByName(any(UUID.class), any(UUID.class));
        verify(fileUtil).stream(any(Path.class));

        // Assertions
    }

    @Test
    void getByName_InlineJPEG_HappyPath() throws FileServerAPIException, MimeTypeException, IOException {
        // Pre defined values

        // Expected Value
        Path filePath = Paths.get("/path/to/file");
        UUID fileId = UUID.randomUUID();
        String extension = "extension";
        MediaType mediaType = MediaType.IMAGE_JPEG;
        String fileName = fileId + "." + extension;

        // Mock data
        UUID folder = UUID.randomUUID();
        FileEntity fileEntity = new FileEntity(filePath, fileId, extension, mediaType);

        // Set up method

        // Stubbing methods
        when(fileService.getByName(any(UUID.class), any(UUID.class))).thenReturn(fileEntity);
        when(fileUtil.stream(any(Path.class))).thenReturn(outputStream -> {
        });

        // Calling the method
        assertDoesNotThrow(() -> mockMvc.perform(get("/folders/{folder}/files/{fileId}", folder, fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(header().string(
                        HttpHeaders.CONTENT_DISPOSITION,
                        String.format("inline; filename=\"%s\"", fileName)
                ))
        );

        // Behavior Verifications
        verify(fileService).getByName(any(UUID.class), any(UUID.class));
        verify(fileUtil).stream(any(Path.class));

        // Assertions
    }

    @Test
    void getByName_AttachmentPDF_HappyPath() throws FileServerAPIException, MimeTypeException, IOException {
        // Pre defined values

        // Expected Value
        Path filePath = Paths.get("/path/to/file");
        UUID fileId = UUID.randomUUID();
        String extension = "extension";
        MediaType mediaType = MediaType.APPLICATION_PDF;
        String fileName = fileId + "." + extension;

        // Mock data
        UUID folder = UUID.randomUUID();
        FileEntity fileEntity = new FileEntity(filePath, fileId, extension, mediaType);

        // Set up method

        // Stubbing methods
        when(fileService.getByName(any(UUID.class), any(UUID.class))).thenReturn(fileEntity);
        when(fileUtil.stream(any(Path.class))).thenReturn(outputStream -> {
        });

        // Calling the method
        assertDoesNotThrow(() -> mockMvc.perform(get("/folders/{folder}/files/{fileId}", folder, fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string(
                        HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"%s\"", fileName)
                ))
        );

        // Behavior Verifications
        verify(fileService).getByName(any(UUID.class), any(UUID.class));
        verify(fileUtil).stream(any(Path.class));

        // Assertions
    }

    @Test
    void isChecksumMatched_HappyPath(@TempDir Path tempDir) throws FileServerAPIException, MimeTypeException, IOException, NoSuchAlgorithmException {
        // Pre defined values

        // Expected Value
        boolean shouldBe = true;

        // Mock data
        FileEntity fileEntity = mock(FileEntity.class);
        String checksum = "checksum";
        UUID folder = UUID.randomUUID();
        UUID fileId = UUID.randomUUID();

        // Set up method

        // Stubbing methods
        when(fileService.getByName(any(UUID.class), any(UUID.class))).thenReturn(fileEntity);
        when(fileEntity.filePath()).thenReturn(tempDir);
        when(fileUtil.checksum(any(Path.class))).thenReturn(checksum);

        // Calling the method
        assertDoesNotThrow(() -> mockMvc.perform(get("/folders/{folder}/files/{fileId}/checksum", folder, fileId)
                        .param("checksum", "checksum"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value(shouldBe))
        );

        // Behavior Verifications
        verify(fileService).getByName(any(UUID.class), any(UUID.class));
        verify(fileUtil).checksum(any(Path.class));

        // Assertions
    }
}