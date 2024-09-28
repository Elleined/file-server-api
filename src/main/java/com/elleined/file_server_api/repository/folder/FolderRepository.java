package com.elleined.file_server_api.repository.folder;

import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Integer> {

    @Query("SELECT folder FROM Folder folder WHERE folder.project = :project AND folder.name = :name")
    Folder findByName(@Param("project") Project project, @Param("name") String name);

    @Query("SELECT folder FROM Folder folder WHERE folder.project = :project")
    Page<Folder> findAll(@Param("project") Project project, Pageable pageable);

    @Query("SELECT folder FROM Folder folder WHERE folder.project = :project")
    List<Folder> findAll(@Param("project") Project project);
}