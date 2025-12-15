package com.workshop.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ExceFileStorageService {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/excel/";

    public String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename with timestamp
        String fileName = System.currentTimeMillis() + "_" + 
                         file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return filePath.toString();
    }

    public boolean deleteFile(String filePath) throws IOException {
        if (filePath == null) return false;
        return Files.deleteIfExists(Paths.get(filePath));
    }

    public byte[] getFile(String filePath) throws IOException {
        if (!Files.exists(Paths.get(filePath))) {
            throw new IOException("File not found: " + filePath);
        }
        return Files.readAllBytes(Paths.get(filePath));
    }

    public List<String> getAllStoredFiles() throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            return Collections.emptyList();
        }
        
        List<String> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(uploadPath)) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    files.add(path.toString());
                }
            }
        }
        return files;
    }
}