package com.aaymecho.slim.demo.service;

import com.aaymecho.slim.demo.model.FileInfo;
import com.aaymecho.slim.demo.web.FileUploadController;
import org.springframework.core.io.Resource;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root = Paths.get("uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!", e);
        }
    }

    public FileInfo save(MultipartFile file) {
        try {
            // Generate unique filename with UUID
            String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), root.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

            // Generate the download/view URL
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileUploadController.class, "serveFile", filename)
                    .build()
                    .toString();

            return new FileInfo(filename, url);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            return new PathResource(file);
        } catch (Exception e) {
            throw new RuntimeException("Could not read the file. Error: " + e.getMessage());
        }
    }
}