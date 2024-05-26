package com.elleined.image_server_api.service.image;

import com.elleined.image_server_api.mapper.image.ImageFormatMapper;
import com.elleined.image_server_api.repository.image.ImageFormatRepository;
import com.elleined.image_server_api.service.image.format.ImageFormatServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageFormatServiceImplTest {

    @Mock
    private ImageFormatRepository imageFormatRepository;
    @Mock
    private ImageFormatMapper imageFormatMapper;

    @InjectMocks
    private ImageFormatServiceImpl imageFormatService;

    @Test
    void save() {
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
    void getById() {
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
    void getAll() {
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
    void isFileExtensionNotValid() {
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