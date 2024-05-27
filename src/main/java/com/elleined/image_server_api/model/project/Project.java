package com.elleined.image_server_api.model.project;

import com.elleined.image_server_api.model.PrimaryKeyIdentity;
import com.elleined.image_server_api.model.PrimaryKeyUUID;
import com.elleined.image_server_api.model.image.ActiveImage;
import com.elleined.image_server_api.model.image.DeletedImage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "tbl_project",
        indexes = { @Index(name = "created_at_idx", columnList = "created_at") }
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
    private List<ActiveImage> activeImages;

    @OneToMany(mappedBy = "project")
    private List<DeletedImage> deletedImages;

    public List<UUID> getAllActiveImageIds() {
        return getActiveImages().stream()
                .map(PrimaryKeyUUID::getId)
                .toList();
    }
    public List<UUID> getAllDeletedImageIds() {
        return getDeletedImages().stream()
                .map(PrimaryKeyUUID::getId)
                .toList();
    }
}
