package com.elleined.file_server_api.file;

import com.elleined.file_server_api.exception.FileServerAPIException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public interface FileUtil {


    static String checksum(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (InputStream is = file.getInputStream()) {
            byte[] buffer = new byte[8192]; // 8kb
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new FileServerAPIException("Error reading file for checksum " + e.getMessage());
        }

        byte[] hash = digest.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    static StreamingResponseBody stream(MultipartFile file) {
        return outputStream -> {
            try (InputStream inputStream = file.getInputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                throw new FileServerAPIException("Error streaming file" + e.getMessage());
            }
        };
    }

    // Remove absolutely everything except visual content
    static void flattenPDF(Path filePath, MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file.getInputStream().readAllBytes())) {

            PDDocumentCatalog catalog = document.getDocumentCatalog();

            // 1. Completely wipe the catalog of everything non-visual
            catalog.setAcroForm(null);
            catalog.setActions(null);
            catalog.setOpenAction(null);
            catalog.setViewerPreferences(null);
            catalog.setDocumentOutline(null);
            catalog.setOCProperties(null);
            catalog.setOutputIntents(Collections.emptyList());
            catalog.setMarkInfo(null);
            catalog.setStructureTreeRoot(null);
            catalog.setLanguage(null);
            catalog.setMetadata(null);

            // 2. Remove all names dictionary entries
            if (catalog.getNames() != null) {
                catalog.setNames(null);
            }

            // 3. Strip all COS object entries that aren't essential for viewing
            COSDictionary catalogDict = catalog.getCOSObject();
            Set<COSName> keysToRemove = new HashSet<>();

            for (COSName key : catalogDict.keySet()) {
                // Keep only absolutely essential keys for PDF viewing
                if (!key.equals(COSName.TYPE) &&
                        !key.equals(COSName.PAGES) &&
                        !key.equals(COSName.VERSION)) {
                    keysToRemove.add(key);
                }
            }

            for (COSName key : keysToRemove) {
                catalogDict.removeItem(key);
            }

            // 4. Process each page - keep only visual content
            for (PDPage page : document.getPages()) {
                // Remove everything from page except content streams and media box
                page.setAnnotations(Collections.emptyList());
                page.setActions(null);

                COSDictionary pageDict = page.getCOSObject();
                Set<COSName> pageKeysToRemove = new HashSet<>();

                for (COSName key : pageDict.keySet()) {
                    // Keep only essential page keys for visual rendering
                    if (!key.equals(COSName.TYPE) &&
                            !key.equals(COSName.PARENT) &&
                            !key.equals(COSName.CONTENTS) &&
                            !key.equals(COSName.MEDIA_BOX) &&
                            !key.equals(COSName.CROP_BOX) &&
                            !key.equals(COSName.BLEED_BOX) &&
                            !key.equals(COSName.TRIM_BOX) &&
                            !key.equals(COSName.ART_BOX) &&
                            !key.equals(COSName.RESOURCES) &&
                            !key.equals(COSName.ROTATE)) {
                        pageKeysToRemove.add(key);
                    }
                }

                for (COSName key : pageKeysToRemove) {
                    pageDict.removeItem(key);
                }

                // 5. Clean up resources dictionary - remove non-visual resources
                PDResources resources = page.getResources();
                if (resources != null) {
                    COSDictionary resourcesDict = resources.getCOSObject();

                    // Remove potentially dangerous resource types
                    resourcesDict.removeItem(COSName.getPDFName("ProcSet"));
                    resourcesDict.removeItem(COSName.getPDFName("Properties"));

                    // Keep only: Font, XObject, ColorSpace, Pattern, Shading, ExtGState
                    // Remove: ProcSet, Properties, and any custom entries
                    Set<COSName> resourceKeysToRemove = new HashSet<>();
                    for (COSName key : resourcesDict.keySet()) {
                        if (!key.equals(COSName.FONT) &&
                                !key.equals(COSName.XOBJECT) &&
                                !key.equals(COSName.COLORSPACE) &&
                                !key.equals(COSName.PATTERN) &&
                                !key.equals(COSName.SHADING) &&
                                !key.equals(COSName.EXT_G_STATE)) {
                            resourceKeysToRemove.add(key);
                        }
                    }

                    for (COSName key : resourceKeysToRemove) {
                        resourcesDict.removeItem(key);
                    }
                }
            }

            // 6. Create completely new, minimal document information
            PDDocumentInformation info = new PDDocumentInformation();
            info.setTitle("Sanitized Document");
            info.setCreator("PDF Sanitizer");
            info.setProducer("PDF Sanitizer");
            document.setDocumentInformation(info);

            // 7. Remove any trailer dictionary entries except essentials
            COSDictionary trailer = document.getDocument().getTrailer();
            Set<COSName> trailerKeysToRemove = new HashSet<>();

            for (COSName key : trailer.keySet()) {
                if (!key.equals(COSName.SIZE) &&
                        !key.equals(COSName.ROOT) &&
                        !key.equals(COSName.INFO) &&
                        !key.equals(COSName.PREV)) {
                    trailerKeysToRemove.add(key);
                }
            }

            for (COSName key : trailerKeysToRemove) {
                trailer.removeItem(key);
            }

            document.save(filePath.toFile());
        } catch (IOException e) {
            throw new IOException("Failed to sanitize PDF: " + e.getMessage(), e);
        }
    }
}
