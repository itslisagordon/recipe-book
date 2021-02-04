package org.liftoff.recipebook.controllers;

import org.liftoff.recipebook.models.Recipe;
import org.liftoff.recipebook.models.RecipeCategory;
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
public class AddProfilePictureController {

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("add-profile-picture")
    public String renderAddProfilePictureForm(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);
        int userId = sessionUser.getId();

        model.addAttribute("recipe", new Recipe());
        model.addAttribute("profile", userRepository.findById(userId).get());
        return "add-profile-picture";
    }

//    @PostMapping("add-profile-picture")
//    public String addProfilePicture(Model model, HttpServletRequest request, @ModelAttribute User user, RedirectAttributes ra) {
//        HttpSession session = request.getSession();
//        User sessionUser = authenticationController.getUserFromSession(session);
//        sessionUser.setProfilePicture(user.getProfilePicture());
//        userRepository.save(sessionUser);
//        return "redirect:/profile/{userId}";
//    }

    @PostMapping("add-profile-picture")
    public String createCreateCategory(@RequestParam String profilePictureUrl, HttpServletRequest request, Model model, RecipeCategory recipeCategory) {
        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);
        int userId = sessionUser.getId();
        sessionUser.setProfilePicture(profilePictureUrl);
        userRepository.save(sessionUser);
        return "redirect:/profile/" + userId;
    }

}
