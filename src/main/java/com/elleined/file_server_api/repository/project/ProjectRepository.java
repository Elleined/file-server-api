package com.elleined.file_server_api.repository.project;

import com.elleined.file_server_api.model.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query("SELECT project FROM Project project WHERE project.name = :name")
    Project findByName(@Param("name") String name);
}