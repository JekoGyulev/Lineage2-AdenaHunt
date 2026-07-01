package main.service.impl;

import main.model.Party;
import main.model.Player;
import main.model.PlayerClass;
import main.property.ClassProperties;
import main.property.ClassProperties.ClassDetails;
import main.repository.PlayerRepository;
import main.security.UserPrincipal;
import main.service.PlayerService;
import main.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import main.property.ClassProperties.Booster;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class PlayerServiceImpl implements PlayerService, UserDetailsService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClassProperties classProperties;


    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, PasswordEncoder passwordEncoder, ClassProperties classProperties) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.classProperties = classProperties;
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
    public void chooseClass(UUID userId, PlayerClass playerClass) {

        Player player = getPlayerById(userId);

        ClassDetails chosenClassDetails = this.classProperties.getDetailsClassByPlayerClass(playerClass);

        setPlayerDetails(player, chosenClassDetails);
    }

    @Override
    public Player getPlayerById(UUID id) {
        return this.playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Player with this ID was not found"));
    }

    @Override
    public List<Booster> getBoosters(PlayerClass playerClass) {
        return this.classProperties.getDetailsClassByPlayerClass(playerClass).getBoosters();
    }

    @Override
    public List<Player> getAllByParty(Party party) {

        if (party == null) {
            return List.of();
        }

        List<Player> partyMembers = this.playerRepository.findAllByPartyIdOrderByJoinedPartyOnDescIdAsc(party.getId());

        return partyMembers;
    }

    @Override
    public List<Player> getAllFreePlayersToInvite(UUID playerId) {
        List<Player> freePlayersToBeInvited = this.playerRepository.findAll()
                .stream()
                .filter(player -> player.getParty() == null)
                .filter( player -> player.getPlayerClass() != null)
                .filter(player -> !player.getId().equals(playerId))
                .toList();

        return freePlayersToBeInvited;
    }

    @Override
    public Player update(Player player) {
        return this.playerRepository.save(player);
    }

    @Override
    public void levelUp(Player player) {
        int nextLevel = (int) (player.getXp() / 100);

        if (nextLevel > player.getLevel()) {
            player.setLevel(nextLevel);

            ClassDetails classDetails = classProperties.getDetailsClassByPlayerClass(player.getPlayerClass());

            player.setHealth(classDetails.getHealthFactor() * nextLevel);
            player.setAttack(classDetails.getAttackFactor() * nextLevel);
            player.setDefense(classDetails.getDefenseFactor() * nextLevel);
        }
    }


    private void setPlayerDetails(Player player, ClassDetails chosenClassDetails) {

        player.setPlayerClass(chosenClassDetails.getPlayerClass());
        player.setBannerImg(chosenClassDetails.getBannerImg());
        player.setAttack(chosenClassDetails.getAttackFactor() * player.getLevel());
        player.setDefense(chosenClassDetails.getDefenseFactor() * player.getLevel());
        player.setHealth(chosenClassDetails.getHealthFactor() * player.getLevel());

        this.playerRepository.save(player);
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
