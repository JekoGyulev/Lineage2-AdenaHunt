package main.service;

import main.model.Player;
import main.model.PlayerClass;
import main.web.dto.LoginRequest;
import main.web.dto.RegisterRequest;

import java.util.UUID;

public interface PlayerService {

    void register(RegisterRequest registerRequest);

    void chooseClass(UUID userId, PlayerClass playerClass);

    Player getPlayerById(UUID id);
}
