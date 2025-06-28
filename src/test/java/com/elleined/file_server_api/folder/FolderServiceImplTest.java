package com.elleined.file_server_api.folder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FolderServiceImplTest {

    @InjectMocks
    private FolderServiceImpl folderService;

    private final Path UPLOAD_PATH = Paths.get("/home/denielle/fsa_uploads");
    private final String FOLDER = "folder";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(folderService, "uploadPath", UPLOAD_PATH.toString());
    }

    @Test
    void create_AndRemove_HappyPath() {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method
        assertDoesNotThrow(() -> folderService.create("uploads/../red"));

        // Behavior Verifications

        // Assertions
    }

    @ParameterizedTest
    @MethodSource("traversalAttachPayloads")
    void create_ShouldNOTThrowFileServerException_ForTraversalAttack(String folder) throws IOException {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method
        folderService.create(folder);

        // Behavior Verifications

        // Assertions
    }

    @Test
    void getUploadPath_HappyPath() {
        // Pre defined values

        // Expected Value

        // Mock data

        // Set up method

        // Stubbing methods

        // Calling the method
        Path path = assertDoesNotThrow(() -> folderService.getUploadPath());

        // Behavior Verifications

        // Assertions
        assertEquals(path, UPLOAD_PATH);
    }

    private static Stream<Arguments> traversalAttachPayloads() {
        return Stream.of(
                Arguments.of("../../etc/passwd"),
                Arguments.of("../../../etc/passwd"),
                Arguments.of("../../../../../../../../etc/shadow"),
                Arguments.of("..\\..\\windows\\system.ini"),
                Arguments.of("..\\..\\..\\..\\boot.ini"),
                Arguments.of("..\\..\\..\\..\\..\\..\\Windows\\System32\\config\\SAM"),
                Arguments.of("..%2f..%2fetc%2fpasswd"),
                Arguments.of("..%5c..%5cwindows%5csystem.ini"),
                Arguments.of("..%252f..%252fetc%252fpasswd"),
                Arguments.of("..%2e%2e%2f..%2e%2e%2fetc%2fpasswd"),
                Arguments.of("..%2e%2e\\..%2e%2e\\windows\\win.ini"),
                Arguments.of("...\\...\\windows\\system32\\drivers\\etc\\hosts"),
                Arguments.of("..\\..\\..\\..\\..\\..\\..\\..\\boot.ini"),
                Arguments.of("..\\../\\..\\../etc/passwd"),
                Arguments.of("%2e%2e%2f%2e%2e%2fetc%2fpasswd"),
                Arguments.of("..%00/etc/passwd"),
                Arguments.of("..\\..\\..\\..\\..\\..\\..\\..\\..\\..\\etc\\passwd"),
                Arguments.of("../../../../../../../../../../etc/passwd")
        );
    }
}