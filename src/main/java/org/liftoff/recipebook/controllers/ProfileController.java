package org.liftoff.recipebook.controllers;

import org.liftoff.recipebook.models.User;
import org.liftoff.recipebook.models.data.UserRepository;
import org.liftoff.recipebook.storage.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.liftoff.recipebook.storage.StorageFileNotFoundException;
import org.liftoff.recipebook.storage.StorageService;


@Controller
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userId}")
    public String displayProfile(Model model, @PathVariable int userId, HttpServletRequest request) {

        Boolean isUserInSession = false;

        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);

        User user = userRepository.findById(userId).get();

        if (userId == sessionUser.getId()) {
            isUserInSession = true;
        }

        model.addAttribute("isUserInSession", isUserInSession);
        model.addAttribute("user", user);
        model.addAttribute("profile", userRepository.findById(userId).get());
        return "profile";
    }

    @PostMapping("/{userId}")
    public String addBio(Model model, HttpServletRequest request, @ModelAttribute User user, RedirectAttributes ra) {
        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);
        sessionUser.setBio(user.getBio());
        userRepository.save(sessionUser);
        return "redirect:/profile/{userId}";
    }

    private final StorageService storageService;

    @Autowired
    public ProfileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(ProfileController.class,
                        "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "profile";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request, @ModelAttribute User user,
                                   RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        sessionUser.setProfilePicture(user.getProfilePicture());
        userRepository.save(sessionUser);
        return "redirect: ";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
