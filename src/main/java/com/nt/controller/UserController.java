package com.nt.controller;

import com.nt.model.User;
import com.nt.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Show the home page after login
    @GetMapping("/home")
    public String showHomePage() {
        return "home";  // Maps to src/main/resources/templates/home.html
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /*@PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.register(user);
        return "redirect:/login";
    }*/

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, HttpSession session) {
        userService.register(user);  // Save the new user
        session.setAttribute("user", user);  // Automatically log in the user
        return "redirect:/home";  // Redirect to home page after successful registration
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    /*@PostMapping("/login")
    public String loginUser(@ModelAttribute User user, HttpSession session) {
        User loggedInUser = userService.login(user.getUsername(), user.getPassword());
        if (loggedInUser != null) {
            session.setAttribute("user", loggedInUser);
            return "redirect:/home";  // Redirect to the home page after login
        } else {
            return "login";  // Login failed
        }
    }*/

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, HttpSession session, Model model) {
        User loggedInUser = userService.login(user.getUsername(), user.getPassword());
        if (loggedInUser != null) {
            session.setAttribute("user", loggedInUser);
            return "redirect:/home";  // Redirect to the home page after successful login
        } else {
            model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
            return "login";  // Stay on the login page and show the error message
        }
    }

    @GetMapping("/users")
    public String listAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/introduction")
    public String showIntroductionPage() {
        return "introduction";  // Maps to src/main/resources/templates/introduction.html
    }

}
