package org.liftoff.recipebook.controllers;

import org.liftoff.recipebook.models.User;
import org.liftoff.recipebook.models.data.RecipeCategoryRepository;
import org.liftoff.recipebook.models.data.RecipeRepository;
import org.liftoff.recipebook.models.Recipe;
import org.liftoff.recipebook.models.RecipeCategory;
import org.liftoff.recipebook.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;



@Controller
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("create-recipe")
    public String renderCreateRecipeForm(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);
        int userId = sessionUser.getId();

        model.addAttribute("recipe",new Recipe());
        model.addAttribute("categories", recipeCategoryRepository.findAll());
        model.addAttribute("profile", userRepository.findById(userId).get());
        return "create-recipe";
    }

    @PostMapping("/create-recipe")
    public String createRecipe(@RequestParam String name, Recipe recipe, @RequestParam String description,
                               @RequestParam String hiddenIngredients, @RequestParam RecipeCategory category,
                               @RequestParam String imageUrl, HttpSession session,Model model){

        //Get the userId from the session
        int currentUserId = (int) session.getAttribute("user");

        //save the recipe to the database
        recipe.setUserId(currentUserId);
        recipe.setImageUrl(imageUrl);
        recipe.setName(name);
        recipe.setDescription(description.trim());//added .trim() to get rid of unnecessary white space
        recipe.setIngredients(hiddenIngredients);
        recipe.setCategory(category);
        recipeRepository.save(recipe);
        User user = userRepository.findById(currentUserId).get();
        model.addAttribute("profile", userRepository.findById(currentUserId).get());
        model.addAttribute("user", user);

        return "view";
    }


    @GetMapping("delete")
    public String displayRemoveRecipe(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);
        int userId = sessionUser.getId();
        model.addAttribute("profile", userRepository.findById(userId).get());
        model.addAttribute("recipesToDelete",recipeRepository.getAllRecipesByUserId(userId));
        return "delete";
    }
    
    @PostMapping("delete")
    public String removeRecipe(@RequestParam int deleteThis, HttpServletRequest request,
                               Model model){
        recipeRepository.deleteById(deleteThis);
        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);
        User user = userRepository.findById(sessionUser.getId()).get();
        int userId = sessionUser.getId();
        model.addAttribute("user", user);
        model.addAttribute("profile", userRepository.findById(userId).get());
        return "profile";
    }
}
