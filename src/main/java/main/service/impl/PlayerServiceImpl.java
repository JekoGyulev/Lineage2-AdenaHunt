package main.service.impl;

import main.exception.LoginException;
import main.model.Player;
import main.repository.PlayerRepository;
import main.security.UserPrincipal;
import main.service.PlayerService;
import main.web.dto.LoginRequest;
import main.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PlayerServiceImpl implements PlayerService, UserDetailsService {

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

    @Override
    public Player login(LoginRequest loginRequest) {
        Player player = this.playerRepository
                .findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new LoginException("Username or password incorrect"));


        if (!passwordEncoder.matches(loginRequest.getPassword(), player.getPassword())) {
            throw new  LoginException("Username or password incorrect");
        }

        return player;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Player> optionalPlayer = this.playerRepository.findByUsername(username);

        if (optionalPlayer.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }

        Player player = optionalPlayer.get();

        UserPrincipal userPrincipal = UserPrincipal.builder()
                .id(player.getId())
                .username(username)
                .password(player.getPassword())
                .build();

        return userPrincipal;
    }
}
