package com.elleined.image_server_api.service.image;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.elleined.image_server_api.model.project.Project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Test
    void createFolders() throws IOException {
        ImageService imageService = new ImageServiceImpl();
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method
        imageService.createFolders(Project.builder()
                .name("sample")
                .build());

        // Behavior Verifications

        // Assertions
    }
}