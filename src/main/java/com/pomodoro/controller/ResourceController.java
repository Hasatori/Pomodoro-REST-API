package com.pomodoro.controller;

import com.pomodoro.model.*;
import com.pomodoro.model.o2auth.FacebookUser;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.h2.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ResourceController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(ResourceController.class);

    @PostMapping("/group/layout/{filename:.+}")
    @ResponseBody
    public  ResponseEntity<Resource> getLayoutImage(@PathVariable String filename) throws IOException {
        Resource resource = storageService.loadAsResource("group/layout/"+filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/users/{id}/profile.jpg")
    @ResponseBody
    public  ResponseEntity<Resource> getProfileImage(@PathVariable Integer id) throws IOException {

        Resource resource = storageService.loadAsResource(String.format("users/%d/profile.jpg",id));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @PostMapping("/users/{id}/avatar.jpg")
    @ResponseBody
    public  ResponseEntity<Resource> getAvatarImage(@PathVariable Integer id) throws IOException {

        Resource resource = storageService.loadAsResource(String.format("users/%d/avatar.jpg",id));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    @PostMapping("/upload")
    @ResponseBody
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {
        String message;
        storageService.store(file);

    }
}

