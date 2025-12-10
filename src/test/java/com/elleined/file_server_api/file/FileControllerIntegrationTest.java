package com.elleined.file_server_api.file;

import com.elleined.file_server_api.folder.FolderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FolderService folderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    private static Stream<Arguments> filePayloads() {
        return Stream.of(
                Arguments.of(FileControllerIntegrationTest.class.getClassLoader().getResourceAsStream("png.png")),
                Arguments.of(FileControllerIntegrationTest.class.getClassLoader().getResourceAsStream("jpg.jpg")),
                Arguments.of(FileControllerIntegrationTest.class.getClassLoader().getResourceAsStream("jpeg.jpeg")),
                Arguments.of(FileControllerIntegrationTest.class.getClassLoader().getResourceAsStream("pdf.pdf"))
        );
    }

    @ParameterizedTest
    @MethodSource("filePayloads")
    void saveFolder_ThenSaveFile_ThenGetByName_ThenVerifyFileChecksum_ThenDelete(InputStream inputStream) throws IOException {
        // Creating the folder
        UUID folder = folderService.save();

        // Checks if the folder really created
        Path folderPath = Paths.get(uploadPath).resolve(folder.toString());
        assertThat(folderPath).exists().isDirectory();

        // Creating the file to be uploaded
        MockMultipartFile file = new MockMultipartFile("file", inputStream);

        // Save the file
        MvcResult mvcResult = assertDoesNotThrow(() -> mockMvc.perform(multipart("/folders/{folder}/files", folder)
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uploaded_at", Matchers.notNullValue()))
                .andExpect(jsonPath("$.folder", Matchers.is(folder.toString())))
                .andExpect(jsonPath("$.folder", Matchers.instanceOf(String.class)))
                .andExpect(jsonPath("$.file_id", Matchers.notNullValue()))
                .andExpect(jsonPath("$.file_id", Matchers.instanceOf(String.class)))
                .andExpect(jsonPath("$.extension", Matchers.notNullValue()))
                .andExpect(jsonPath("$.extension", Matchers.instanceOf(String.class)))
                .andExpect(jsonPath("$.media_type", Matchers.notNullValue()))
                .andExpect(jsonPath("$.media_type", Matchers.instanceOf(String.class)))
                .andExpect(jsonPath("$.checksum", Matchers.notNullValue()))
                .andExpect(jsonPath("$.checksum", Matchers.instanceOf(String.class)))
                .andExpect(jsonPath("$.file_name", Matchers.notNullValue()))
                .andExpect(jsonPath("$.file_name", Matchers.instanceOf(String.class)))
                .andReturn()
        );
        // API Response of saving the file
        FileDTO fileDTO = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), FileDTO.class);

        // Checks if uploaded file really exists
        Path filePath = folderPath.resolve(fileDTO.getFileName());
        assertThat(filePath).exists();

        // Get the file
        assertDoesNotThrow(() -> mockMvc.perform(get("/folders/{folder}/files/{file}", fileDTO.folder(), fileDTO.fileId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(fileDTO.mediaType()))
        );

        // Verify file checksum
        assertDoesNotThrow(() -> mockMvc.perform(get("/folders/{folder}/files/{file}/verify", fileDTO.folder(), fileDTO.fileId())
                        .param("checksum", fileDTO.checksum()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.is(true)))
        );

        // Delete the file
        assertDoesNotThrow(() -> mockMvc.perform(delete("/folders/{folder}/files/{file}", fileDTO.folder(), fileDTO.fileId()))
                .andExpect(status().isOk())
        );

        assertThat(filePath).doesNotExist();
    }
}