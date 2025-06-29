package com.elleined.file_server_api.file;

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
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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

        MultipartFile file = new MockMultipartFile("attachment", "attachment.png", "text/plain", getClass().getClassLoader().getResourceAsStream("linux-mint.png"));

        // Set up method

        // Stubbing methods
        when(folderService.getByName(any(UUID.class))).thenReturn(Paths.get("/home/denielle/fsa_uploads/f312e1a0-88e7-4282-982d-aab09b286358"));


        // Calling the method
        assertDoesNotThrow(() -> fileService.save(UUID.fromString("f312e1a0-88e7-4282-982d-aab09b286358"), file));

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