package main.web;


import main.model.Player;
import main.security.UserPrincipal;
import main.service.PartyService;
import main.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/party")
public class PartyController {

    private final PartyService partyService;
    private final PlayerService playerService;


    @Autowired
    public PartyController(PartyService partyService, PlayerService playerService) {
        this.partyService = partyService;
        this.playerService = playerService;
    }


    @PostMapping
    public String invitePlayer(@RequestParam(value = "receiverId") UUID invitedPlayerId,
                               @AuthenticationPrincipal UserPrincipal userPrincipal) {
        UUID senderId = userPrincipal.getId();

        this.partyService.invitePlayer(senderId, invitedPlayerId);

        return "redirect:/lobby";
    }



}
