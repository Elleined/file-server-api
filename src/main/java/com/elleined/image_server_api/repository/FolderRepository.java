package com.elleined.image_server_api.repository;

import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FolderRepository extends JpaRepository<Folder, Integer> {

    @Query("SELECT f.activeImages FROM Folder f WHERE f = :folder")
    Page<ActiveImage> findAllActiveImages(@Param("folder") Folder folder, Pageable pageable);

    @Query("SELECT f.deletedImages FROM Folder f WHERE f = :folder")
    Page<DeletedImage> findAllDeletedImages(@Param("folder") Folder folder, Pageable pageable);
}