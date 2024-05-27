package com.elleined.image_server_api.controller.image;

import com.elleined.image_server_api.dto.image.ImageFormatDTO;
import com.elleined.image_server_api.mapper.image.ImageFormatMapper;
import com.elleined.image_server_api.model.image.ImageFormat;
import com.elleined.image_server_api.service.image.format.ImageFormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
    public List<ImageFormatDTO> getAll(@RequestParam(required = false, defaultValue = "1", value = "pageNumber") int pageNumber,
                                       @RequestParam(required = false, defaultValue = "5", value = "pageSize") int pageSize,
                                       @RequestParam(required = false, defaultValue = "ASC", value = "sortDirection") Sort.Direction direction,
                                       @RequestParam(required = false, defaultValue = "id", value = "sortBy") String sortBy) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, direction, sortBy);
        return imageFormatService.getAll(pageable).stream()
                .map(imageFormatMapper::toDTO)
                .toList();
    }
}
