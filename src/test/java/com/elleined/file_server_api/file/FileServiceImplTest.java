package com.elleined.file_server_api.file;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.elleined.file_server_api.folder.FolderService;
import org.apache.tika.Tika;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private FolderService folderService;

    @Spy
    private Tika tika;

    @InjectMocks
    private FileServiceImpl fileService;

    @Test
    void save_HappyPath() throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data
        MultipartFile file = new MockMultipartFile("attachment", "attachment.jpg", "text/plain", getClass().getClassLoader().getResourceAsStream("linux-mint.png"));

        // Set up method

        // Stubbing methods

        // Calling the method
        assertDoesNotThrow(() -> fileService.save("folder", file));

        // Behavior Verifications

        // Assertions
    }

    @Test
    void deleteByName_HappyPath() {
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
}