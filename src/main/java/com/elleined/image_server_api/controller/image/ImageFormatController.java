package com.elleined.image_server_api.controller.image;

import com.elleined.image_server_api.dto.image.ImageFormatDTO;
import com.elleined.image_server_api.mapper.image.ImageFormatMapper;
import com.elleined.image_server_api.model.image.ImageFormat;
import com.elleined.image_server_api.service.image.format.ImageFormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image-formats")
public class ImageFormatController {
    private final ImageFormatService imageFormatService;
    private final ImageFormatMapper imageFormatMapper;

    @GetMapping("/{id}")
    public ImageFormatDTO getById(@PathVariable("id") int id) {
        ImageFormat imageFormat = imageFormatService.getById(id);
        return imageFormatMapper.toDTO(imageFormat);
    }

    @GetMapping
    public List<ImageFormatDTO> getAll() {
        return imageFormatService.getAll().stream()
                .map(imageFormatMapper::toDTO)
                .toList();
    }
}
