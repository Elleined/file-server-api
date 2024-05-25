package com.elleined.image_server_api.service.image;

import com.elleined.image_server_api.model.image.ImageFormat;

import java.util.List;

public interface ImageFormatService {
    ImageFormat save(String format);
    ImageFormat getById(int id);
    List<ImageFormat> getAll();

    default List<ImageFormat> saveAll(List<String> formats) {
        return formats.stream()
                .map(this::save)
                .toList();
    }
}
