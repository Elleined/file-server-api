package com.elleined.file_server_api.repository.project;

import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("SELECT p.folders FROM Project p WHERE p = :project")
    Page<Folder> findAllFolders(@Param("project") Project project, Pageable pageable);
}