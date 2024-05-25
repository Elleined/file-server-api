package com.elleined.image_server_api.populator;

import com.elleined.image_server_api.mapper.image.ImageFormatMapper;
import com.elleined.image_server_api.service.image.ImageFormatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Transactional
public class ImageFormatPopulator extends Populator {
    private final ImageFormatService imageFormatService;
    private final ImageFormatMapper imageFormatMapper;

    public ImageFormatPopulator(ObjectMapper objectMapper, ImageFormatService imageFormatService, ImageFormatMapper imageFormatMapper) {
        super(objectMapper);
        this.imageFormatService = imageFormatService;
        this.imageFormatMapper = imageFormatMapper;
    }

    @Override
    public void populate(String jsonFile) throws IOException {
        var resource = new ClassPathResource(jsonFile);
        byte[] dataBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        var type = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);

        List<String> formats = objectMapper.readValue(new String(dataBytes, StandardCharsets.UTF_8), type);
        imageFormatService.saveAll(formats);
    }
}
