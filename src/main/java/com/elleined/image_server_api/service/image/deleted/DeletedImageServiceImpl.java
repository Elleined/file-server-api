package com.elleined.image_server_api.service.image.deleted;

import com.elleined.image_server_api.exception.resource.ResourceNotFoundException;
import com.elleined.image_server_api.model.PrimaryKeyUUID;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.repository.image.DeletedImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeletedImageServiceImpl implements DeletedImageService {
    private final DeletedImageRepository deletedImageRepository;

    @Override
    public List<DeletedImage> getAllByUUID(List<UUID> uuids) {
        List<DeletedImage> deletedImages = deletedImageRepository.findAllById(uuids).stream()
                .sorted(Comparator.comparing(PrimaryKeyUUID::getCreatedAt).reversed())
                .toList();

        deletedImages.forEach(deletedImage -> deletedImage.setLastAccessedAt(LocalDateTime.now()));
        deletedImageRepository.saveAll(deletedImages);

        return deletedImages;
    }

    @Override
    public DeletedImage getByUUID(UUID uuid) {
        return deletedImageRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Deleted image with uuid of " + uuid + " does not exists!"));
    }

    @Override
    public void deleteAll() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<DeletedImage> deletedImages = deletedImageRepository.findAll().stream()
                .filter(deletedImage -> deletedImage.getLastAccessedAt().isBefore(oneMonthAgo) ||
                        deletedImage.getLastAccessedAt().equals(oneMonthAgo))
                .toList();
    }
}
