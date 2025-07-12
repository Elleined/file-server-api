package com.elleined.file_server_api.folder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public interface FolderService {
    UUID save() throws IOException;

    Path getByName(UUID folder) throws IOException;
}
