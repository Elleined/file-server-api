package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import com.elleined.file_server_api.file.util.FileUtil;
import org.apache.tika.mime.MimeTypeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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

    @TempDir
    private Path tempDir;

    private static Stream<Arguments> getByName_HappyPath_Payload() {
        return Stream.of(
                Arguments.of(MediaType.IMAGE_PNG, "inline", "png"),
                Arguments.of(MediaType.IMAGE_JPEG, "inline", "jpeg"),
                Arguments.of(MediaType.APPLICATION_PDF, "attachment", "pdf")
        );
    }

    @Test
    void save_HappyPath() throws IOException, FileServerAPIException, MimeTypeException, NoSuchAlgorithmException {
        // Pre defined values

        // Expected Value

        // Mock data
        MockMultipartFile file = mock(MockMultipartFile.class);
        FileDTO fileDTO = mock(FileDTO.class);

        // Set up method
        when(file.getName()).thenReturn("file");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));

        // Stubbing methods
        when(fileService.save(any(UUID.class), any(MultipartFile.class))).thenReturn(fileDTO);

        // Calling the method
        assertDoesNotThrow(() -> mockMvc.perform(multipart("/folders/{folder}/files", UUID.randomUUID().toString())
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        );

        // Behavior Verifications
        verify(fileService).save(any(UUID.class), any(MultipartFile.class));

        // Assertions
    }

    @ParameterizedTest
    @MethodSource("getByName_HappyPath_Payload")
    void getByName_HappyPath(MediaType mediaType, String contentDisposition, String extension) throws FileServerAPIException, MimeTypeException, IOException {
        // Pre defined values

        // Expected Value
        UUID folder = UUID.randomUUID();
        UUID fileId = UUID.randomUUID();
        String fileName = fileId + "." + extension;

        // Mock data
        FileEntity fileEntity = mock(FileEntity.class);
        when(fileEntity.filePath()).thenReturn(tempDir);
        when(fileEntity.mediaType()).thenReturn(mediaType.toString());
        when(fileEntity.getContentDisposition()).thenReturn(contentDisposition);
        when(fileEntity.getFileName()).thenReturn(fileName);

        // Set up method

        // Stubbing methods
        when(fileService.getByUUID(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(fileEntity));
        when(fileUtil.stream(any(Path.class))).thenReturn(outputStream -> {
        });

        // Calling the method
        assertDoesNotThrow(() -> mockMvc.perform(get("/folders/{folder}/files/{fileId}", folder, fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(header().string(
                        HttpHeaders.CONTENT_DISPOSITION,
                        String.format("%s; filename=\"%s\"", contentDisposition, fileName)
                ))
        );

        // Behavior Verifications
        verify(fileService).getByUUID(any(UUID.class), any(UUID.class));
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
        when(fileService.getByUUID(any(UUID.class), any(UUID.class))).thenReturn(Optional.of(fileEntity));
        when(fileEntity.filePath()).thenReturn(tempDir);
        when(fileUtil.checksum(any(Path.class))).thenReturn(checksum);

        // Calling the method
        assertDoesNotThrow(() -> mockMvc.perform(get("/folders/{folder}/files/{fileId}/verify", folder, fileId)
                        .param("checksum", checksum))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isBoolean())
                .andExpect(jsonPath("$").value(shouldBe))
        );

        // Behavior Verifications
        verify(fileService).getByUUID(any(UUID.class), any(UUID.class));
        verify(fileUtil).checksum(any(Path.class));

        // Assertions
    }
}