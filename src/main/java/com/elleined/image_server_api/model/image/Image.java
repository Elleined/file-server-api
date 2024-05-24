package com.elleined.image_server_api.model.image;

import com.elleined.image_server_api.model.PrimaryKeyIdentity;
import com.elleined.image_server_api.model.project.Project;
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
public class Image extends PrimaryKeyIdentity {

    @Column(
            name = "uuid",
            nullable = false,
            updatable = false,
            unique = true
    )
    private String uuid;

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

    @Lob
    @Column(
            name = "image",
            nullable = false
    )
    private byte[] image;
}
