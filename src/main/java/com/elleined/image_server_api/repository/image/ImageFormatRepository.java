package com.elleined.image_server_api.repository.image;

import com.elleined.image_server_api.model.image.ImageFormat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFormatRepository extends JpaRepository<ImageFormat, Integer> {
}