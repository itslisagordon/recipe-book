package org.liftoff.recipebook.controllers;

import org.liftoff.recipebook.models.Recipe;
import org.liftoff.recipebook.models.User;
import org.liftoff.recipebook.models.data.RecipeRepository;
import org.liftoff.recipebook.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AddProfilePictureController {

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping("/{userId}")
    public String displayProfile(Model model, @PathVariable int userId, HttpServletRequest request) {

        HttpSession session = request.getSession();
        User user = userRepository.findById(userId).get();
        User sessionUser = authenticationController.getUserFromSession(session);
        Boolean isUserInSession = false;

        Iterable<Recipe> userRecipes = recipeRepository.getAllRecipesByUserId(userId);


        if (userId == sessionUser.getId()) {
            isUserInSession = true;
        }

        model.addAttribute("isUserInSession", isUserInSession);
        model.addAttribute("user", user);
        model.addAttribute("profile", userRepository.findById(userId).get());
        model.addAttribute("userRecipes", recipeRepository.getAllRecipesByUserId(userId));


        return "profile";
    }

}
