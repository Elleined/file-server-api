package com.elleined.image_server_api;

import com.elleined.image_server_api.populator.ImageFormatPopulator;
import com.elleined.image_server_api.repository.image.ImageFormatRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AfterStartUp {
    private final ImageFormatRepository imageFormatRepository;

    private final ImageFormatPopulator imageFormatPopulator;

    @PostConstruct
    void init() throws IOException {
        if (imageFormatRepository.existsById(1)) {
            System.out.println("Returning because pre-defined values are already been saved!");
            return;
        }

        System.out.println("Saving pre-defined values... Please wait");
        imageFormatPopulator.populate("/json/image_formats.json");
        System.out.println("Saving pre-defined values... Completed.");
    }
}
