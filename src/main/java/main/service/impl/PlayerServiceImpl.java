package main.service.impl;

import main.model.Player;
import main.repository.PlayerRepository;
import main.service.PlayerService;
import main.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, PasswordEncoder passwordEncoder) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(RegisterRequest registerRequest) {

        Player player = Player
                .builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .nickname(registerRequest.getNickname())
                .level(1)
                .xp(100)
                .build();


        this.playerRepository.save(player);
    }
}
