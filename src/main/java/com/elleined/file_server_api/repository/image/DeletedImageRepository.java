package com.elleined.file_server_api.repository.image;

import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.file.DeletedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DeletedImageRepository extends JpaRepository<DeletedFile, UUID> {

    @Query("SELECT di FROM DeletedImage di WHERE di.folder = :folder")
    Page<DeletedFile> findAll(@Param("folder") Folder folder, Pageable pageable);

}