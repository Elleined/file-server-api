package com.elleined.image_server_api.model.folder;

import com.elleined.image_server_api.model.PrimaryKeyIdentity;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import com.elleined.image_server_api.model.project.Project;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(
        name = "tbl_folder",
        indexes = {
                @Index(name = "created_at_idx", columnList = "created_at"),
                @Index(name = "name_idx", columnList = "name")
        }
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Folder extends PrimaryKeyIdentity {

    @Column(
            name = "name",
            nullable = false,
            unique = true
    )
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "project_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Project project;

    @OneToMany(mappedBy = "folder")
    private List<ActiveImage> activeImages;

    @OneToMany(mappedBy = "folder")
    private List<DeletedImage> deletedImages;
}
