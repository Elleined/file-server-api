package com.elleined.image_server_api.repository.image;

import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DeletedImageRepository extends JpaRepository<DeletedImage, Integer> {


    @Query("SELECT di FROM DeletedImage di WHERE di.uuid = :uuid")
    Optional<DeletedImage> fetchByUUID(@Param("uuid") String uuid);
}