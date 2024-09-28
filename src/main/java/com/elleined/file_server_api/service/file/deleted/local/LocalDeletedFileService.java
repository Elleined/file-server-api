package com.elleined.file_server_api.service.file.deleted.local;

import com.elleined.file_server_api.model.file.DeletedFile;
import com.elleined.file_server_api.model.folder.Folder;
import com.elleined.file_server_api.model.project.Project;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LocalDeletedFileService {
    byte[] getImage(Project project, Folder folder, String fileName) throws IOException;
    void transfer(Project project, Folder folder, MultipartFile file) throws IOException;
    void permanentlyDeleteDeletedImages(List<DeletedFile> deletedImages) throws IOException;
}
