package com.elleined.image_server_api.service.image.deleted;

import com.elleined.image_server_api.model.PrimaryKeyIdentity;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.repository.image.DeletedImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeletedImageServiceImpl implements DeletedImageService {
    private final DeletedImageRepository deletedImageRepository;

    @Override
    public List<DeletedImage> getAllById(List<Integer> ids) {
        List<DeletedImage> deletedImages = deletedImageRepository.findAllById(ids).stream()
                .sorted(Comparator.comparing(PrimaryKeyIdentity::getCreatedAt).reversed())
                .toList();

        deletedImages.forEach(deletedImage -> deletedImage.setLastAccessedAt(LocalDateTime.now()));
        deletedImageRepository.saveAll(deletedImages);

        return deletedImages;
    }

    @Override
    public void deleteAll() {

    }
}
