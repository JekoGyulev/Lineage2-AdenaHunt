package main.web;


import jakarta.servlet.http.HttpSession;
import main.model.Mob;
import main.model.Player;
import main.security.UserPrincipal;
import main.service.MobService;
import main.service.PlayerService;
import main.web.dto.FightResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/mobs")
public class MobController {

    private final MobService mobService;
    private final PlayerService playerService;

    @Autowired
    public MobController(MobService mobService, PlayerService playerService) {
        this.mobService = mobService;
        this.playerService = playerService;
    }


    @GetMapping
    public ModelAndView getFarmZonePage(@AuthenticationPrincipal UserPrincipal userPrincipal, HttpSession session) {

        Player currentPlayer = this.playerService.getPlayerById(userPrincipal.getId());

        List<Mob> sortedMobs = this.mobService.getAllMobs();

        FightResult fightResult = (FightResult) session.getAttribute("fightResult");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("farm-zone");
        modelAndView.addObject("currentPlayer", currentPlayer);
        modelAndView.addObject("mobs", sortedMobs);
        modelAndView.addObject("fightResult", fightResult);

        session.removeAttribute("fightResult");

        return modelAndView;
    }

    @PatchMapping("/{mobId}")
    public String attackMob(@PathVariable UUID mobId, @AuthenticationPrincipal UserPrincipal userPrincipal, HttpSession session) {

        Player player = this.playerService.getPlayerById(userPrincipal.getId());

        FightResult fightResult = this.mobService.attackMob(mobId, player);

        session.setAttribute("fightResult", fightResult);

        return "redirect:/mobs";
    }









}
