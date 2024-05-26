package com.elleined.image_server_api.service.image.format;

import com.elleined.image_server_api.exception.resource.ResourceAlreadyExistsException;
import com.elleined.image_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.image_server_api.mapper.image.ImageFormatMapper;
import com.elleined.image_server_api.model.PrimaryKeyIdentity;
import com.elleined.image_server_api.model.image.ImageFormat;
import com.elleined.image_server_api.repository.image.ImageFormatRepository;
import com.elleined.image_server_api.validator.FieldValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageFormatServiceImpl implements ImageFormatService {
    private final ImageFormatRepository imageFormatRepository;
    private final ImageFormatMapper imageFormatMapper;

    private final FieldValidator fieldValidator;

    @Override
    public ImageFormat save(String format) {
        if (isAlreadyExists(format))
            throw new ResourceAlreadyExistsException(STR."Cannot save format! because \{format} already exists");

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

    @Override
    public Optional<ImageFormat> getByMultipart(MultipartFile image) {
        String fileExtension = FilenameUtils.getExtension(image.getOriginalFilename());
        return imageFormatRepository.findAll().stream()
                .filter(imageFormat -> imageFormat.getFormat().equalsIgnoreCase(fileExtension))
                .findFirst();
    }

    @Override
    public boolean isAlreadyExists(String format) {
        return imageFormatRepository.findAll().stream()
                .map(ImageFormat::getFormat)
                .anyMatch(format::equalsIgnoreCase);
    }

    @Override
    public boolean isFileExtensionValid(MultipartFile image) {
        String fileExtension = FilenameUtils.getExtension(image.getOriginalFilename());
        if (fieldValidator.isNotValid(fileExtension)) return false;
        return imageFormatRepository.findAll().stream()
                .map(ImageFormat::getFormat)
                .map(String::toLowerCase)
                .anyMatch(fileExtension::equalsIgnoreCase);
    }
}
