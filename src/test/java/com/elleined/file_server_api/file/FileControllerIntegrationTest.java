    package com.elleined.file_server_api.file;

    import com.elleined.file_server_api.folder.FolderService;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import org.junit.jupiter.api.Test;
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
    import java.nio.file.Files;
    import java.nio.file.LinkOption;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.util.UUID;

    import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
    import static org.junit.jupiter.api.Assertions.assertTrue;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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

        private static final InputStream pngFile = FileControllerIntegrationTest.class.getClassLoader().getResourceAsStream("png.png");

        @Test
        void saveFolder_TheSaveFile_ThenGetByName_ThenVerifyFileChecksum() throws IOException {
            // Creating the folder
            UUID folder = folderService.save();

            // Checks if the folder really created
            Path folderPath = Paths.get(uploadPath).resolve(folder.toString());
            assertTrue(Files.exists(folderPath, LinkOption.NOFOLLOW_LINKS), "Folder not created");

            // Creating the file to be uploaded
            MockMultipartFile file = new MockMultipartFile("file", pngFile);

            // Save the file
            MvcResult saveMvcResult = assertDoesNotThrow(() -> mockMvc.perform(multipart("/folders/{folder}/files", folder)
                            .file(file))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.uploadedAt").exists())
                    .andExpect(jsonPath("$.folder").exists())
                    .andExpect(jsonPath("$.folder").isString())
                    .andExpect(jsonPath("$.folder").value(folder.toString()))
                    .andExpect(jsonPath("$.fileId").exists())
                    .andExpect(jsonPath("$.fileId").isString())
                    .andExpect(jsonPath("$.extension").exists())
                    .andExpect(jsonPath("$.extension").isString())
                    .andExpect(jsonPath("$.mediaType").exists())
                    .andExpect(jsonPath("$.mediaType").isString())
                    .andExpect(jsonPath("$.checksum").exists())
                    .andExpect(jsonPath("$.checksum").isString())
                    .andExpect(jsonPath("$.fileName").exists())
                    .andExpect(jsonPath("$.fileName").isString())
                    .andReturn()
            );
            // API Response of saving the file
            FileDTO fileDTO = objectMapper.readValue(saveMvcResult.getResponse().getContentAsString(), FileDTO.class);

            // Checks if uploaded file really exists
            Path filePath = folderPath.resolve(fileDTO.getFileName());
            assertTrue(Files.exists(filePath, LinkOption.NOFOLLOW_LINKS), "File not created");

            // Get the file
            assertDoesNotThrow(() -> mockMvc.perform(get("/folders/{folder}/files/{fileId}", fileDTO.folder(), fileDTO.fileId()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(fileDTO.mediaType()))
            );

            // Verify file checksum
            assertDoesNotThrow(() -> mockMvc.perform(get("/folders/{folder}/files/{fileId}/verify", folder, fileDTO.fileId())
                            .param("checksum", fileDTO.checksum()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$").isBoolean())
                    .andExpect(jsonPath("$").value(true))
            );
        }
    }