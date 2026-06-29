package main.security;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.model.Player;
import main.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final PlayerService playerService;

    @Autowired
    public CustomAuthenticationSuccessHandler(PlayerService playerService) {
        this.playerService = playerService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Player player = this.playerService.getPlayerById(userPrincipal.getId());

        if (player.getPlayerClass() == null) {
            response.sendRedirect("/class");
        } else {
            response.sendRedirect("/lobby");
        }
    }
}
