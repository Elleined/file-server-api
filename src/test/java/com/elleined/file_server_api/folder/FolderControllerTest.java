package com.elleined.file_server_api.folder;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FolderController.class)
class FolderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FolderService folderService;

    @Test
    void save_HappyPath() throws IOException {
        // Pre defined values

        // Expected Value
        UUID folder = UUID.randomUUID();

        // Mock data

        // Set up method

        // Stubbing methods
        when(folderService.save()).thenReturn(folder);

        // Calling the method
        assertDoesNotThrow(() -> mockMvc.perform(post("/folders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.notNullValue()))
                .andExpect(jsonPath("$", Matchers.is(folder.toString()))));

        // Behavior Verifications
        verify(folderService).save();

        // Assertions
    }

    @Test
    void delete_HappyPath() throws IOException {
        // Pre defined values

        // Expected Value
        UUID folder = UUID.randomUUID();

        // Mock data

        // Set up method

        // Stubbing methods
        doNothing().when(folderService).delete(any(UUID.class));

        // Calling the method
        assertDoesNotThrow(() -> mockMvc.perform(delete("/folders/{folder}", folder)).andExpect(status().isOk()));

        // Behavior Verifications
        verify(folderService).delete(any(UUID.class));

        // Assertions
    }
}