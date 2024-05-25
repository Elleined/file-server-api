package com.elleined.image_server_api.controller.image;

import com.elleined.image_server_api.dto.image.DeletedImageDTO;
import com.elleined.image_server_api.mapper.image.DeletedImageMapper;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.service.image.deleted.DeletedImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/deleted-images")
public class DeletedImageController {
    private final DeletedImageService deletedImageService;
    private final DeletedImageMapper deletedImageMapper;

    @GetMapping("/get-all-by-id")
    public List<DeletedImageDTO> getAllById(List<Integer> ids) {
        return deletedImageService.getAllById(ids).stream()
                .map(deletedImageMapper::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public DeletedImageDTO getById(@PathVariable("id") int id) {
        DeletedImage deletedImage = deletedImageService.getById(id);
        return deletedImageMapper.toDTO(deletedImage);
    }
}
