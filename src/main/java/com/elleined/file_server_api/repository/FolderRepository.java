package com.elleined.file_server_api.repository;

import com.elleined.file_server_api.model.file.ActiveFile;
import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.file.DeletedFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FolderRepository extends JpaRepository<Folder, Integer> {

    @Query("SELECT f.activeImages FROM Folder f WHERE f = :folder")
    Page<ActiveFile> findAllActiveImages(@Param("folder") Folder folder, Pageable pageable);

    @Query("SELECT f.deletedImages FROM Folder f WHERE f = :folder")
    Page<DeletedFile> findAllDeletedImages(@Param("folder") Folder folder, Pageable pageable);
}