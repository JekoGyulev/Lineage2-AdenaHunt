package main.web;

import jakarta.validation.Valid;
import main.model.Mob;
import main.model.Player;
import main.property.ClassProperties;
import main.security.UserPrincipal;
import main.service.MobService;
import main.service.PlayerService;
import main.web.dto.LoginRequest;
import main.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import main.property.ClassProperties.Booster;

import java.util.List;

@Controller
public class IndexController {

    private final PlayerService playerService;
    private final MobService mobService;

    @Autowired
    public IndexController(PlayerService playerService, MobService mobService) {
        this.playerService = playerService;
        this.mobService = mobService;
    }

    @GetMapping
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());

        return modelAndView;
    }

    @PostMapping("/register")
    public String register(@Valid RegisterRequest registerRequest,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        this.playerService.register(registerRequest);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("loginRequest", new LoginRequest());

        return modelAndView;
    }


    @GetMapping("/lobby")
    public ModelAndView getLobbyPage(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        Player player = this.playerService.getPlayerById(userPrincipal.getId());

        List<Booster> boosters = this.playerService.getBoosters(player.getPlayerClass());

        List<Mob> top3RecentMobs = this.mobService.getTop3RecentMobs();

        List<Player> partyMembers = this.playerService.getAllByParty(player.getParty());

        List<Player> playersToBeInvited = this.playerService.getAllFreePlayersToInvite(player.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("lobby");
        modelAndView.addObject("currentPlayer", player);
        modelAndView.addObject("boosters", boosters);
        modelAndView.addObject("recentMobs", top3RecentMobs);
        modelAndView.addObject("partyMembers", partyMembers);
        modelAndView.addObject("playersToBeInvited", playersToBeInvited);

        return modelAndView;
    }



}
