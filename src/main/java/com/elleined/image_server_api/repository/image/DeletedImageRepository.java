package com.elleined.image_server_api.repository.image;

import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DeletedImageRepository extends JpaRepository<DeletedImage, UUID> {

    @Query("SELECT di FROM DeletedImage di WHERE di.folder = :folder")
    Page<DeletedImage> findAll(@Param("folder") Folder folder, Pageable pageable);

}