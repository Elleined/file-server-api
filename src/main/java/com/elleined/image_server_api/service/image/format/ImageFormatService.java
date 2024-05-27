package com.elleined.image_server_api.service.image.format;

import com.elleined.image_server_api.model.image.ImageFormat;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ImageFormatService {
    ImageFormat save(String format);
    ImageFormat getById(int id);
    List<ImageFormat> getAll(Pageable pageable);

    Optional<ImageFormat> getByMultipart(MultipartFile image);

    boolean isAlreadyExists(String format);
    boolean isFileExtensionValid(MultipartFile image);

    default List<ImageFormat> saveAll(List<String> formats) {
        return formats.stream()
                .map(this::save)
                .toList();
    }
}
