package org.liftoff.recipebook.controllers;

import org.liftoff.recipebook.models.User;
import org.liftoff.recipebook.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userId}")
    public String displayProfile(Model model, @PathVariable int userId, HttpServletRequest request) {

        HttpSession session = request.getSession();
        User user = userRepository.findById(userId).get();
        User sessionUser = authenticationController.getUserFromSession(session);
        Boolean isUserInSession = false;

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
}
