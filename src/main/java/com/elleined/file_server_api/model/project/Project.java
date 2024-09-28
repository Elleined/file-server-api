package com.elleined.file_server_api.model.project;

import com.elleined.file_server_api.model.PrimaryKeyIdentity;
import com.elleined.file_server_api.model.folder.Folder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(
        name = "tbl_project",
        indexes = {
                @Index(name = "created_at_idx", columnList = "created_at"),
                @Index(name = "name_idx", columnList = "name")
        }
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Project extends PrimaryKeyIdentity {

    @Column(
            name = "name",
            nullable = false,
            unique = true
    )
    private String name;

    @OneToMany(mappedBy = "project")
    private List<Folder> folders;

    public boolean has(Integer folderId) {
        return this.getFolders().stream()
                .map(PrimaryKeyIdentity::getId)
                .anyMatch(folderId::equals);
    }
}
