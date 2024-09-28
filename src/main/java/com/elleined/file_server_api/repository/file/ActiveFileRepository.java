package com.elleined.file_server_api.repository.file;

import com.elleined.file_server_api.model.file.ActiveFile;
import com.elleined.file_server_api.model.folder.Folder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ActiveFileRepository extends JpaRepository<ActiveFile, UUID> {

    @Query("SELECT ai FROM ActiveFile ai WHERE ai.folder = :folder")
    Page<ActiveFile> findAll(@Param("folder") Folder folder, Pageable pageable);

}