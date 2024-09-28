package com.elleined.file_server_api.service.format;

import com.elleined.file_server_api.exception.resource.ResourceAlreadyExistsException;
import com.elleined.file_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.file_server_api.mapper.format.FormatMapper;
import com.elleined.file_server_api.model.format.Format;
import com.elleined.file_server_api.repository.image.ImageFormatRepository;
import com.elleined.file_server_api.validator.FieldValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FormatServiceImpl implements FormatService {
    private final ImageFormatRepository imageFormatRepository;
    private final FormatMapper formatMapper;

    private final FieldValidator fieldValidator;

    @Override
    public Format save(String format) {
        if (isAlreadyExists(format))
            throw new ResourceAlreadyExistsException("Cannot save format! because " + format + " already exists");

        Format imageFormat = formatMapper.toEntity(format);
        imageFormatRepository.save(imageFormat);
        log.debug("Saving image format {} success", format);
        return imageFormat;
    }

    @Override
    public Format getById(int id) {
        return imageFormatRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image format with id of " + id + " does not exists!"));
    }

    @Override
    public Page<Format> getAll(Pageable pageable) {
        return imageFormatRepository.findAll(pageable);
    }

    @Override
    public Optional<Format> getByMultipart(MultipartFile image) {
        String fileExtension = FilenameUtils.getExtension(image.getOriginalFilename());
        return imageFormatRepository.findAll().stream()
                .filter(imageFormat -> imageFormat.getFormat().equalsIgnoreCase(fileExtension))
                .findFirst();
    }

    @Override
    public boolean isAlreadyExists(String format) {
        return imageFormatRepository.findAll().stream()
                .map(Format::getFormat)
                .anyMatch(format::equalsIgnoreCase);
    }

    @Override
    public boolean isFileExtensionValid(MultipartFile image) {
        String fileExtension = FilenameUtils.getExtension(image.getOriginalFilename());
        if (fieldValidator.isNotValid(fileExtension)) return false;
        return imageFormatRepository.findAll().stream()
                .map(Format::getFormat)
                .map(String::toLowerCase)
                .anyMatch(fileExtension::equalsIgnoreCase);
    }

    @Override
    public List<Format> saveAll(List<String> formats) {
        if (formats.stream().anyMatch(this::isAlreadyExists))
            throw new ResourceAlreadyExistsException("Cannot save all formats! because one of the specified format already exists!");

        return formats.stream()
                .map(this::save)
                .toList();
    }
}
