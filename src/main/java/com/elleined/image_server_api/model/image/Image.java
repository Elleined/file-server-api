package com.elleined.image_server_api.model.image;

import com.elleined.image_server_api.model.PrimaryKeyIdentity;
import com.elleined.image_server_api.model.PrimaryKeyUUID;
import jakarta.persistence.*;
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
            name = "image_format_id",
            referencedColumnName = "id",
            nullable = false,
            updatable = false
    )
    private ImageFormat imageFormat;

    @Column(
            name = "file_name",
            nullable = false,
            unique = true
    )
    private String fileName;
}
