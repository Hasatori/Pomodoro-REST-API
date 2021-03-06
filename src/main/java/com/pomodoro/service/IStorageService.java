package com.pomodoro.service;

import com.pomodoro.model.group.Group;
import com.pomodoro.utils.SizeUnit;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

@Service
@Transactional
public interface IStorageService {



    void init();

    String store(MultipartFile file);

    String store(MultipartFile file,String absolutePath);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    long getGroupAttachmentsSize(Group group, SizeUnit sizeUnit);
}
