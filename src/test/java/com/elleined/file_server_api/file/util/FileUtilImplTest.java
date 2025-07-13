package com.elleined.file_server_api.file.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileUtilImplTest {

    private final InputStream pdfFile = getClass().getClassLoader().getResourceAsStream("pdf.pdf");
    private final InputStream pngFile = getClass().getClassLoader().getResourceAsStream("png.png");
    @InjectMocks
    private FileUtilImpl fileUtil;

    @Test
    void getFileExtension_HappyPath() {
        // Pre defined values

        // Expected Value
        String expectedExtension = "pdf";

        // Mock data
        MediaType mediaType = MediaType.APPLICATION_PDF;

        // Set up method

        // Stubbing methods
        String actual = assertDoesNotThrow(() -> fileUtil.getFileExtension(mediaType));

        // Calling the method

        // Behavior Verifications

        // Assertions
        assertEquals(expectedExtension, actual);
    }

    @Test
    void checksum_HappyPath() throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data
        MultipartFile file = new MockMultipartFile("file", pdfFile);

        // Set up method

        // Stubbing methods

        // Calling the method
        String expected = assertDoesNotThrow(() -> fileUtil.checksum(file));
        String actual = assertDoesNotThrow(() -> fileUtil.checksum(file));

        // Behavior Verifications

        // Assertions
        assertEquals(expected, actual);
    }

    @Test
    void checksum_ShouldReturnFalse_IfFileHasBeenModified() throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data
        MultipartFile pdfFile = new MockMultipartFile("file", this.pdfFile);
        MultipartFile pngFile = new MockMultipartFile("file", this.pngFile);

        // Set up method

        // Stubbing methods

        // Calling the method
        String expected = assertDoesNotThrow(() -> fileUtil.checksum(pdfFile));
        String actual = assertDoesNotThrow(() -> fileUtil.checksum(pngFile));

        // Behavior Verifications

        // Assertions
        assertNotEquals(expected, actual);
    }

    @Test
    void stream_HappyPath() {
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