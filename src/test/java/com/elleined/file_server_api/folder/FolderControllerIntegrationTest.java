package com.elleined.file_server_api.folder;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FolderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${UPLOAD_PATH}")
    private String uploadPath;

    @Test
    void save_HappyPath() throws UnsupportedEncodingException {
        // API Call to create the folder
        MvcResult mvcResult = assertDoesNotThrow(() -> mockMvc.perform(post("/folders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$", not(empty())))
                .andReturn());

        // Check if folder really exists
        UUID folder = UUID.fromString(mvcResult.getResponse().getContentAsString());
        assertThat(Paths.get(uploadPath).resolve(folder.toString())).exists().isDirectory();
    }
}