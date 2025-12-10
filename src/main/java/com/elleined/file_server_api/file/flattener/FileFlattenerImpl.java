
package com.elleined.file_server_api.file.flattener;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

@Service
public class FileFlattenerImpl implements FileFlattener {
    @Override
    public void flattenImage(Path filePath, MultipartFile file, String realExtension) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage image = ImageIO.read(inputStream);
            ImageIO.write(image, realExtension, filePath.toFile());
        }
    }
}
