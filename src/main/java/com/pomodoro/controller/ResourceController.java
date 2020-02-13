package com.pomodoro.controller;

import com.pomodoro.model.*;
import com.pomodoro.model.o2auth.FacebookUser;
import com.pomodoro.utils.DateUtils;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.h2.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.mail.Multipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ResourceController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(ResourceController.class);

    @PostMapping("/group/layout/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getLayoutImage(@PathVariable String filename) throws IOException {
        Resource resource = storageService.loadAsResource("group/layout/" + filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/users/{id}/profile.jpg")
    @ResponseBody
    public ResponseEntity<Resource> getProfileImage(@PathVariable Integer id) throws IOException {

        Resource resource = storageService.loadAsResource(String.format("users/%d/profile.jpg", id));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/users/{id}/avatar.jpg")
    @ResponseBody
    public ResponseEntity<Resource> getAvatarImage(@PathVariable Integer id) throws IOException {

        Resource resource = storageService.loadAsResource(String.format("users/%d/avatar.jpg", id));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/group/{id}/attachment/{attachmentId}")
    @ResponseBody
    public ResponseEntity<Resource> getAttachment(@PathVariable("id") Integer groupId, @PathVariable("attachmentId") String attachmentId) throws IOException {
        Resource resource = storageService.loadAsResource(String.format("group/%s/attachment/%s", groupId, attachmentId));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/group/{id}/attachment/{attachmentId}/download")
    @ResponseBody
    public void downloadAttachment(@PathVariable("id") Integer groupId, @PathVariable("attachmentId") String attachmentId, HttpServletResponse response) throws IOException {
        File file = storageService.loadAsResource(String.format("group/%s/attachment/%s", groupId, attachmentId)).getFile();
        if (file.exists()) {

            //get the mimetype
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                //unknown mimetype so set the mimetype to application/octet-stream
                mimeType = "application/octet-stream";
            }

            response.setContentType(mimeType);
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

            //Here we have mentioned it to show as attachment
            //response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

            response.setContentLength((int) file.length());

            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
    }

    @PostMapping("/upload")
    @ResponseBody
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {
        String message;
        storageService.store(file);

    }

    @PostMapping("/group/{groupName}/chat/attachment")
    public ResponseEntity<GroupMessage> attachFileToChat(HttpServletRequest req, @PathVariable String groupName, @RequestParam("file") MultipartFile file) throws IOException {
        User author = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Group group = groupRepository.findPomodoroGroupByName(groupName).get(0);
        GroupMessage groupMessage = new GroupMessage();
        List<String> groupAttachments = group.getGroupMessages().stream().map(GroupMessage::getAttachment).collect(Collectors.toList());
        UUID uniqueKey = UUID.randomUUID();
        while (groupAttachments.contains(uniqueKey.toString())) {
            uniqueKey = UUID.randomUUID();
        }
        String attachmentValue = String.format("%s.%s", uniqueKey.toString(), FilenameUtils.getExtension(file.getResource().getFilename()));
        storageService.store(file, String.format("group/%d/attachment/%s", group.getId(), attachmentValue));
        groupMessage.setAuthor(author);
        groupMessage.setAuthorId(author.getId());
        groupMessage.setValue(file.getResource().getFilename());
        groupMessage.setTimestamp(DateUtils.getCurrentDateUtc());
        groupMessage.setGroup(group);
        groupMessage.setGroupId(group.getId());
        groupMessage.setRelatedGroupMessages(new ArrayList<>());
        groupMessage.setAttachment(attachmentValue);
        groupMessage = groupMessageRepository.save(groupMessage);
        for (User user : groupMessage.getGroup().getUsers()) {
            UserGroupMessage userGroupMessage = new UserGroupMessage();
            userGroupMessage.setUser(user);
            if (user.getUsername().equals(author.getUsername())) {
                userGroupMessage.setReadTimestamp(DateUtils.getCurrentDateUtc());
            }
            userGroupMessage.setGroupMessage(groupMessage);

            groupMessage.getRelatedGroupMessages().add(userGroupMessage);
        }


        return ResponseEntity.ok().body(groupMessageRepository.save(groupMessage));
    }
}

