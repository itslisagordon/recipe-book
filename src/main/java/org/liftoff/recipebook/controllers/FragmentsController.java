package org.liftoff.recipebook.controllers;

import org.liftoff.recipebook.models.User;
import org.liftoff.recipebook.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class FragmentsController {

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/fragments")
    public String displayMyProfileLink(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);
        User sessionUserFromRepo = userRepository.findById(sessionUser.getId()).get();

        model.addAttribute("sessionUser", sessionUserFromRepo);
        return "fragments";
    }

}
