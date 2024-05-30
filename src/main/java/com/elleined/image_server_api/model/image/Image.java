package com.elleined.image_server_api.model.image;

import com.elleined.image_server_api.model.PrimaryKeyUUID;
import com.elleined.image_server_api.model.folder.Folder;
import com.elleined.image_server_api.model.format.Format;
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
public abstract class Image extends PrimaryKeyUUID {

    @Column(
            name = "last_accessed_at",
            nullable = false
    )
    private LocalDateTime lastAccessedAt;

    @Column(name = "description")
    private String description;

    @Column(name = "additional_information")
    private String additionalInformation;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "format_id",
            referencedColumnName = "id",
            nullable = false,
            updatable = false
    )
    private Format format;

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
}
