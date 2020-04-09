package com.pomodoro.controller;

import com.pomodoro.model.group.Group;
import com.pomodoro.model.message.GroupMessage;
import com.pomodoro.model.user.User;
import com.pomodoro.utils.RequestDataNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;

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
    public ResponseEntity<GroupMessage> attachFileToChat(HttpServletRequest req, @PathVariable String groupName, @RequestParam("file") MultipartFile file) throws IOException, RequestDataNotValidException {
        User author = userService.getUserFromToken(userService.getTokenFromRequest(req));
        Group group = groupService.getGroup(author, groupName);
        return ResponseEntity.ok().body(groupService.createGroupMessageAttachment(author,group,file));
    }
}

