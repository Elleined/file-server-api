package com.elleined.image_server_api.repository.project;

import com.elleined.image_server_api.model.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}