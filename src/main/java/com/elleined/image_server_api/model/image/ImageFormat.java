package com.elleined.image_server_api.model.image;


import com.elleined.image_server_api.model.PrimaryKeyIdentity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tbl_image_format")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ImageFormat extends PrimaryKeyIdentity {

    @Column(
            name = "format",
            nullable = false,
            unique = true
    )
    private String format;
}
