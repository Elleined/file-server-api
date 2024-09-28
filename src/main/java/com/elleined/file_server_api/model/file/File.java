package com.elleined.file_server_api.model.file;

import com.elleined.file_server_api.model.PrimaryKeyUUID;
import com.elleined.file_server_api.model.folder.Folder;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class File extends PrimaryKeyUUID {

    @Column(
            name = "last_accessed_at",
            nullable = false
    )
    private LocalDateTime lastAccessedAt;

    @Column(name = "description")
    private String description;

    @Column(name = "additional_information")
    private String additionalInformation;

    @Column(
            name = "file_name",
            nullable = false,
            unique = true
    )
    private String fileName;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "folder_id",
            referencedColumnName = "id",
            nullable = false,
            updatable = false
    )
    private Folder folder;

    @Column(
            name = "file_size_in_mb",
            nullable = false,
            updatable = false
    )
    private double fileSizeInMB;
}
