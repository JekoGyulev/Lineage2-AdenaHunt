package main.web;


import main.model.PlayerClass;
import main.security.UserPrincipal;
import main.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ClassController {

    private final PlayerService playerService;


    @Autowired
    public ClassController(PlayerService playerService) {
        this.playerService = playerService;
    }


    @GetMapping("/class")
    public ModelAndView getClassPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("classes");

        return modelAndView;
    }

    @PatchMapping("/players/me/class")
    public String chooseClass(@RequestParam("selectedClass") PlayerClass playerClass,
                              @AuthenticationPrincipal UserPrincipal userPrincipal) {


        this.playerService.chooseClass(userPrincipal.getId(), playerClass);


        return "redirect:/lobby";
    }




}
