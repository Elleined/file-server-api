package com.elleined.image_server_api.repository.image;

import com.elleined.image_server_api.model.format.Format;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFormatRepository extends JpaRepository<Format, Integer> {
}