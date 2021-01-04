package org.liftoff.recipebook.controllers;

import org.liftoff.recipebook.models.User;
import org.liftoff.recipebook.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    AuthenticationController authenticationController;

    @GetMapping
    public String displayProfile(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);
        String username = user.getUsername();
        String bio = user.getBio();
        model.addAttribute("username", username);
        model.addAttribute("bio", bio);
        return "profile";
    }

    public String checkBio(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);
        String bio = user.getBio();
        boolean bioExists = false;
        if (!user.getBio().trim().equals("")) {
            bioExists = true;
        }
        model.addAttribute("bioExists", bioExists);
        return "profile";
    }

    @PostMapping("profile")
    public String addBio(Model model, HttpServletRequest request, @RequestParam String bio) {
        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);
        user.setBio(bio);
        return "redirect:";
    }


}
