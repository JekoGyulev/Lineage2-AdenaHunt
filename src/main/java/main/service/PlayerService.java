package main.service;

import main.model.Player;
import main.web.dto.LoginRequest;
import main.web.dto.RegisterRequest;

public interface PlayerService {

    void register(RegisterRequest registerRequest);

    Player login(LoginRequest loginRequest);
}
