package com.elleined.file_server_api;

import com.elleined.file_server_api.populator.FormatPopulator;
import com.elleined.file_server_api.repository.image.ImageFormatRepository;
import com.elleined.file_server_api.service.folder.FolderCreator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AfterStartUp {
    private final ImageFormatRepository imageFormatRepository;

    private final FolderCreator folderCreator;

    private final FormatPopulator formatPopulator;

    @PostConstruct
    void init() throws IOException {
        folderCreator.createFolder();
        if (imageFormatRepository.existsById(1)) {
            System.out.println("Returning because pre-defined values are already been saved!");
            return;
        }

        System.out.println("Saving pre-defined values... Please wait");
        formatPopulator.populate("/json/image_formats.json");

        System.out.println("Saving pre-defined values... Completed.");
    }
}
