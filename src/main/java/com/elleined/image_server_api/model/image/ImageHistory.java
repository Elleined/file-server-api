package com.elleined.image_server_api.model.image;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "tbl_image_history",
        indexes = {
                @Index(name = "created_at_idx", columnList = "created_at"),
                @Index(name = "uuid_idx", columnList = "uuid")
        }
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ImageHistory extends Image {

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "image_id",
            referencedColumnName = "id",
            nullable = false,
            updatable = false
    )
    private Image image;
}
