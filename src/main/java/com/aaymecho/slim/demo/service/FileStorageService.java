package com.aaymecho.slim.demo.service;


import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;

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

}
