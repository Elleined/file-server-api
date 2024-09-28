package com.elleined.file_server_api.repository.image;

import com.elleined.file_server_api.model.format.Format;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFormatRepository extends JpaRepository<Format, Integer> {
}