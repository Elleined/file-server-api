package com.elleined.image_server_api.repository.image;

import com.elleined.image_server_api.model.image.ActiveImage;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ActiveImageRepository extends JpaRepository<ActiveImage, UUID> {

}