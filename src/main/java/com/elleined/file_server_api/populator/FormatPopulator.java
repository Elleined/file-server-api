package com.elleined.file_server_api.populator;

import com.elleined.file_server_api.service.format.FormatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@Transactional
public class FormatPopulator extends Populator {
    private final FormatService formatService;

    public FormatPopulator(ObjectMapper objectMapper, FormatService formatService) {
        super(objectMapper);
        this.formatService = formatService;
    }

    @Override
    public void populate(String jsonFile) throws IOException {
        var resource = new ClassPathResource(jsonFile);
        byte[] dataBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        var type = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);

        List<String> formats = objectMapper.readValue(new String(dataBytes, StandardCharsets.UTF_8), type);
        formatService.saveAll(formats);
    }
}