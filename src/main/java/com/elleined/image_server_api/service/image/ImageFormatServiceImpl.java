package com.elleined.image_server_api.service.image;

import com.elleined.image_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.image_server_api.mapper.image.ImageFormatMapper;
import com.elleined.image_server_api.model.PrimaryKeyIdentity;
import com.elleined.image_server_api.model.image.ImageFormat;
import com.elleined.image_server_api.repository.image.ImageFormatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageFormatServiceImpl implements ImageFormatService {
    private final ImageFormatRepository imageFormatRepository;
    private final ImageFormatMapper imageFormatMapper;

    @Override
    public ImageFormat save(String format) {
        ImageFormat imageFormat = imageFormatMapper.toEntity(format);
        imageFormatRepository.save(imageFormat);
        log.debug("Saving image format {} success", format);
        return imageFormat;
    }

    @Override
    public ImageFormat getById(int id) {
        return imageFormatRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(STR."Image format with id of \{id} does not exists!"));
    }

    @Override
    public List<ImageFormat> getAll() {
        return imageFormatRepository.findAll().stream()
                .sorted(Comparator.comparing(PrimaryKeyIdentity::getId))
                .toList();
    }
}
