package com.elleined.file_server_api.file;

import com.elleined.file_server_api.folder.FolderService;
import org.apache.tika.Tika;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.unit.DataSize;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private FolderService folderService;

    @Spy
    private Tika tika;

    @InjectMocks
    private FileServiceImpl fileService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fileService, "maxFileSize", DataSize.ofMegabytes(3));
    }


    @Test
    void save_HappyPath() throws IOException {
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