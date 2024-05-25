package com.elleined.image_server_api.repository.image;

import com.elleined.image_server_api.model.image.ActiveImage;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ActiveImageRepository extends JpaRepository<ActiveImage, Integer> {

    @Query("SELECT ActiveImage ai FROM ActiveImage ai WHERE ai.uuid = :uuid")
    Optional<ActiveImage> fetchByUUID(@Param("uuid") String uuid);
}