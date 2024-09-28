package com.elleined.file_server_api.model.file;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "tbl_image",
        indexes = {
                @Index(name = "created_at_idx", columnList = "created_at"),
                @Index(name = "file_name_idx", columnList = "file_name")
        }
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ActiveFile extends File {

}
