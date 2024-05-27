package com.elleined.image_server_api.repository.project;

import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("SELECT p.activeImages FROM Project p")
    Page<ActiveImage> findAllActiveImages(@Param("project") Project project, Pageable pageable);


    @Query("SELECT p.deletedImages FROM Project p")
    Page<DeletedImage> findAllDeletedImages(@Param("project") Project project, Pageable pageable);
}