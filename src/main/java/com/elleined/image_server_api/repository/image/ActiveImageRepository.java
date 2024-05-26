package com.elleined.image_server_api.repository.image;

import com.elleined.image_server_api.model.image.ActiveImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActiveImageRepository extends JpaRepository<ActiveImage, UUID> {

}