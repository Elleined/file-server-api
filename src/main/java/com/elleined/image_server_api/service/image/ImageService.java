package com.elleined.image_server_api.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    int getSize(MultipartFile image);
    String getFormat(MultipartFile image);
}
