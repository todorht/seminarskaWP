package mk.ukim.finki.deals_n_steals.web.controller;

import mk.ukim.finki.deals_n_steals.model.enumeration.Role;
import mk.ukim.finki.deals_n_steals.model.exception.InvalidArgumentsException;
import mk.ukim.finki.deals_n_steals.model.exception.PasswordDoNotMatchException;
import mk.ukim.finki.deals_n_steals.service.AuthService;
import mk.ukim.finki.deals_n_steals.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;

    }


    @PostMapping
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam Role role){
        try {
            this.userService.register(username, password, name, surname, role);
        }catch (PasswordDoNotMatchException | InvalidArgumentsException exception){
            return "redirect:/register?error=" + exception.getMessage();
        }
        return "redirect:/login";
    }

    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error, Model model){
        if(error != null && !error.isEmpty()){
            model.addAttribute("hasError", true);
            model.addAttribute("error",error);
        }
        model.addAttribute("bodyContent","register");
        return "master-details";
    }
}