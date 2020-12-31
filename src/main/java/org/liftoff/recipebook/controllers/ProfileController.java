package org.liftoff.recipebook.controllers;

import org.liftoff.recipebook.models.User;
import org.liftoff.recipebook.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("profile")
public class ProfileController {

//    @Autowired
//    private UserRepository userRepository;
//
//    private static final String userSessionKey = "user";
//
//    public User getUserFromSession(HttpSession session, Model model) {
//        Integer userId = (Integer) session.getAttribute(userSessionKey);
//        if (userId == null) {
//            return null;
//        }
//
//        Optional<User> user = userRepository.findById(userId);
//
//        String username = user.get().getUsername();
//
//        if(user.isEmpty()) {
//            return null;
//        }
//
//        model.addAttribute("username", username);
//        return user.get();
//    }

    @Autowired
    AuthenticationController authenticationController;

    @GetMapping
    public String displayProfile(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);
        String username = user.getUsername();
        model.addAttribute("username", username);
        return "profile";
    }




}
