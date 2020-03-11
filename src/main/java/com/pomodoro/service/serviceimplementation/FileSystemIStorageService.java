package com.pomodoro.service.serviceimplementation;


import com.pomodoro.model.Group;
import com.pomodoro.service.IStorageService;
import com.pomodoro.service.StorageException;
import com.pomodoro.service.StorageProperties;
import com.pomodoro.utils.SizeUnit;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service("basicStorageService")
public class FileSystemIStorageService implements IStorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemIStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {

        }
    }

    @Override
    public String store(MultipartFile file) {
        return store(file, StringUtils.cleanPath(file.getOriginalFilename()));
    }

    @Override
    public String store(MultipartFile file, String absolutePath) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + absolutePath);
            }
            if (absolutePath.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + absolutePath);
            }
            this.rootLocation.resolve(absolutePath).toFile().mkdirs();
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(absolutePath),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + absolutePath, e);
        }

        return absolutePath;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException(
                        "Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageException("Could not find file " + filename);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public long getGroupAttachmentsSize(Group group, SizeUnit sizeUnit) {
        File file = rootLocation.resolve(String.format("group/%d/attachment", group.getId())).toFile();

        if (file.exists() && file.isDirectory()) {
            long sizeInBytes = FileUtils.sizeOfDirectory(file);
            return sizeInBytes / sizeUnit.getInByte();
        }
        return 0;
    }
}
