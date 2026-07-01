package main.service;


import main.model.Party;
import main.model.Player;
import main.model.PlayerClass;
import main.property.ClassProperties.Booster;
import main.web.dto.RegisterRequest;

import java.util.List;
import java.util.UUID;

public interface PlayerService {

    void register(RegisterRequest registerRequest);

    void chooseClass(UUID userId, PlayerClass playerClass);

    Player getPlayerById(UUID id);

    List<Booster> getBoosters(PlayerClass playerClass);

    List<Player> getAllByParty(Party party);

    List<Player> getAllFreePlayersToInvite(UUID playerId);

    Player update(Player player);
}
